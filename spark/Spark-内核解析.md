# Spark-内核解析

### yarn部署流程源码

yarn.pom

```
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-yarn_2.11</artifactId>
            <version>2.1.1</version>
        </dependency>
```



#### SparkSubmit源码 

```scala
  override def main(args: Array[String]): Unit = {
    val appArgs = new SparkSubmitArguments(args)
    if (appArgs.verbose) {
      // scalastyle:off println
      printStream.println(appArgs)
      // scalastyle:on println
    }
    appArgs.action match {
      case SparkSubmitAction.SUBMIT => submit(appArgs)
      case SparkSubmitAction.KILL => kill(appArgs)
      case SparkSubmitAction.REQUEST_STATUS => requestStatus(appArgs)
    }
  }
```



```scala
1. SparkSubmit//源码 
    // 启动进程
    -- main
        // 封装参数 命令行带入的参数
        -- new SparkSubmitArguments
        // 提交
        -- submit
            // 准备提交环境
            -- prepareSubmitEnvironment
                // Cluster
                -- childMainClass = "org.apache.spark.deploy.yarn.Client"
                // Client
                -- childMainClass = args.mainClass (SparkPi)
            
            -- doRunMain (runMain)
            
                // 反射加载类
                -- Utils.classForName(childMainClass)
                // 查找main方法
                -- mainClass.getMethod("main", new Array[String](0).getClass)
                // 调用main方法
                -- mainMethod.invoke
                
2. Client
    -- main
        -- new ClientArguments(argStrings)
        -- new Client
            -- yarnClient = YarnClient.createYarnClient
        -- client.run
                -- submitApplication
                    // 封装指令 command = bin/java org.apache.spark.deploy.yarn.ApplicationMaster (Cluster)
                    // command = bin/java org.apache.spark.deploy.yarn.ExecutorLauncher  (client)
                    -- createContainerLaunchContext
                    -- createApplicationSubmissionContext
                
                    // 向Yarn提交应用，提交指令
                    -- yarnClient.submitApplication(appContext)
```

#### ApplicationMaster源码 

```scala
1. ApplicationMaster
    // 启动进程
    -- main
        -- new ApplicationMasterArguments(args)
        // 创建应用管理器对象
        -- new ApplicationMaster(amArgs, new YarnRMClient)
        // 运行
        -- master.run
            // Cluster集群的时候
            -- runDriver
                // 启动用户应用
                -- startUserApplication
                
                    // 获取用户应用的类的main方法
                    -- userClassLoader.loadClass(args.userClass)
      .getMethod("main", classOf[Array[String]])
      
                    // 启动Driver线程，执行用户类的main方法，
                    -- new Thread().start()
                // 注册AM
                -- registerAM
                    // 获取yarn资源
                    -- client.register
                    // 分配资源
                    -- allocator.allocateResources()
                        -- handleAllocatedContainers
                            -- runAllocatedContainers
                                -- new ExecutorRunnable().run
                                    -- startContainer
                                        // command = bin/java org.apache.spark.executor.CoarseGrainedExecutorBackend
                                        -- prepareCommand
```

#### CoarseGrainedExecutorBackend源码

```scala
1. CoarseGrainedExecutorBackend
    -- main
        -- run
            -- onStart
								// 反向注册
                -- ref.ask[Boolean](RegisterExecutor
            -- receive
                // 反馈
                --  case RegisteredExecutor
                    -- new Executor
```

### Spark核心组件

#### Driver

​		Spark驱动器节点，用于执行Spark任务中的main方法，负责实际代码的执行工作。Driver在Spark作业执行时主要负责：

1. 将用户程序转化为作业（job）；

2. 在Executor之间调度任务(task)；

3. 跟踪Executor的执行情况；
4. 通过UI展示查询运行情况；

#### Executor

Spark Executor节点是一个JVM进程，应该是一个计算对象，在`CoarseGrainedExecutorBackend`当中，负责在 Spark 作业中运行具体任务，任务彼此之间相互独立。Spark 应用启动时，Executor节点被同时启动，并且始终伴随着整个 Spark 应用的生命周期而存在。**如果有Executor节点发生了故障或崩溃，Spark 应用也可以继续执行，会将出错节点上的任务调度到其他Executor节点上继续运行。**

Executor有两个核心功能：

1. 负责运行组成Spark应用的任务，并将结果返回给驱动器进程；

2. 它们通过自身的块管理器（Block Manager）为用户程序中要求缓存的 RDD 提供内存式存储。RDD 是直接缓存在Executor进程内的，因此任务可以在运行时充分利用缓存数据加速运算。

#### Spark通用运行流程概述

![img](/Users/wangfulin/github/image/spark/wps2qXkVF.png)

​		任务提交后，都会先启动Driver进程，随后Driver进程向集群管理器注册应用程序，之后集群管理器根据此任务的配置文件分配Executor并启动，**当Driver所需的资源全部满足后，Driver开始执行main函数**，Spark查询为懒执行，**当执行到action算子时开始反向推算**，根据宽依赖进行stage的划分，随后每一个stage对应一个taskset，taskset中有多个task，根据本地化原则，task会被分发到指定的Executor去执行，在任务执行的过程中，Executor也会不断与Driver进行通信，报告任务运行情况。

### Spark部署模式

Spark支持3种集群管理器（Cluster Manager），分别为：

1. Standalone：独立模式，Spark原生的简单集群管理器，自带完整的服务，可单独部署到一个集群中，无需依赖任何其他资源管理系统，使用Standalone可以很方便地搭建一个集群；

2. Apache Mesos：一个强大的分布式资源管理框架，它允许多种不同的框架部署在其上，包括yarn；

3. Hadoop YARN：统一的资源管理机制，在上面可以运行多套计算框架，如map reduce、storm等，根据driver在集群中的位置不同，分为yarn client和yarn cluster。

​        实际上，除了上述这些通用的集群管理器外，Spark内部也提供了一些方便用户测试和学习的简单集群部署模式。由于在实际工厂环境下使用的绝大多数的集群管理器是Hadoop YARN，因此我们关注的重点是Hadoop YARN模式下的Spark集群部署。

​        Spark的运行模式取决于传递给SparkContext的MASTER环境变量的值，个别模式还需要辅助的程序接口来配合使用，目前支持的Master字符串及URL包括：

表2-1 Spark运行模式配置

|    Master URL     |                           Meaning                            |
| :---------------: | :----------------------------------------------------------: |
|       local       |        在本地运行，只有一个工作进程，无并行计算能力。        |
|     local[K]      |  在本地运行，有K个工作进程，通常设置K为机器的CPU核心数量。   |
|     local[*]      |       在本地运行，工作进程数量等于机器的CPU核心数量。        |
| spark://HOST:PORT | 以Standalone模式运行，这是Spark自身提供的集群运行模式，默认端口号: 7077。详细文档见:Spark standalone cluster。 |
| mesos://HOST:PORT | 在Mesos集群上运行，Driver进程和Worker进程运行在Mesos集群上，部署模式必须使用固定值:--deploy-mode cluster。详细文档见:MesosClusterDispatcher. |
|    yarn-client    | 在Yarn集群上运行，Driver进程在本地，Executor进程在Yarn集群上，部署模式必须使用固定值:--deploy-mode client。Yarn集群地址必须在HADOOP_CONF_DIR or YARN_CONF_DIR变量里定义。 |
|   yarn-cluster    | 在Yarn集群上运行，Driver进程在Yarn集群上，Work进程也在Yarn集群上，部署模式必须使用固定值:--deploy-mode cluster。Yarn集群地址必须在HADOOP_CONF_DIR or YARN_CONF_DIR变量里定义。 |

用户在提交任务给Spark处理时，以下两个参数共同决定了Spark的运行方式。

· –master MASTER_URL ：决定了Spark任务提交给哪种集群处理。

· –deploy-mode DEPLOY_MODE：决定了Driver的运行方式，可选值为Client或者Cluster。

### Standalone模式运行机制

Standalone集群有四个重要组成部分，分别是：

1) Driver：是一个进程，我们编写的Spark应用程序就运行在Driver上，由Driver进程执行；

2) Master(RM)：是一个进程，主要负责资源的调度和分配，并进行集群的监控等职责；

3) Worker(NM)：是一个进程，一个Worker运行在集群中的一台服务器上，主要负责两个职责，一个是用自己的内存存储RDD的某个或某些partition；另一个是启动其他进程和线程（Executor），对RDD上的partition进行并行的处理和计算。

4) Executor：是一个进程，一个Worker上可以运行多个Executor，Executor通过启动多个线程（task）来执行对RDD的partition进行并行计算，也就是执行我们对RDD定义的例如map、flatMap、reduce等算子操作。

#### Standalone Client模式

![img](/Users/wangfulin/github/image/spark/wpsazIyET.png)

​		在Standalone Client模式下，Driver在任务提交的本地机器上运行，Driver启动后向Master注册应用程序，Master根据submit脚本的资源需求找到内部资源至少可以启动一个Executor的所有Worker，然后在这些Worker之间分配Executor，Worker上的Executor启动后会向Driver反向注册，所有的Executor注册完成后，Driver开始执行main函数，之后执行到Action算子时，开始划分stage，每个stage生成对应的taskSet，之后将task分发到各个Executor上执行。

#### Standalone Cluster模式

![img](/Users/wangfulin/github/image/spark/wpsQvftpZ.png)

​		在Standalone Cluster模式下，任务提交后，Master会找到一个Worker启动Driver进程，Driver启动后向Master注册应用程序，Master根据submit脚本的资源需求找到内部资源至少可以启动一个Executor的所有Worker，然后在这些Worker之间分配Executor，Worker上的Executor启动后会向Driver反向注册，所有的Executor注册完成后，Driver开始执行main函数，之后执行到Action算子时，开始划分stage，每个stage生成对应的taskSet，之后将task分发到各个Executor上执行。

注意，Standalone的两种模式下（client/Cluster），Master在接到Driver注册Spark应用程序的请求后，会获取其所管理的剩余资源能够启动一个Executor的所有Worker，然后在这些Worker之间分发Executor，此时的分发只考虑Worker上的资源是否足够使用，直到当前应用程序所需的所有Executor都分配完毕，Executor反向注册完毕后，Driver开始执行main程序。

### YARN模式运行机制

#### YARN Client模式

![img](/Users/wangfulin/github/image/spark/wpsvncvXx.png)

​		在YARN Client模式下，Driver在任务提交的本地机器上运行，Driver启动后会和ResourceManager通讯申请启动ApplicationMaster，随后ResourceManager分配container，在合适的NodeManager上启动ApplicationMaster，此时的ApplicationMaster的功能相当于一个ExecutorLaucher，只负责向ResourceManager申请Executor内存。

​		ResourceManager接到ApplicationMaster的资源申请后会分配container，然后ApplicationMaster在资源分配指定的NodeManager上启动Executor进程，Executor进程启动后会向Driver反向注册，Executor全部注册完成后Driver开始执行main函数，之后执行到Action算子时，触发一个job，并根据宽依赖开始划分stage，每个stage生成对应的taskSet，之后将task分发到各个Executor上执行。



#### YARN Cluster模式

![img](/Users/wangfulin/github/image/spark/wpsvgyomg.png)

​        在YARN Cluster模式下，任务提交后会和ResourceManager通讯申请启动ApplicationMaster，随后ResourceManager分配container，在合适的NodeManager上启动ApplicationMaster，此时的ApplicationMaster就是Driver。

​        Driver启动后向ResourceManager申请Executor内存，ResourceManager接到ApplicationMaster的资源申请后会分配container，然后在合适的NodeManager上启动Executor进程，Executor进程启动后会向Driver反向注册，Executor全部注册完成后Driver开始执行main函数，之后执行到Action算子时，触发一个job，并根据宽依赖开始划分stage，每个stage生成对应的taskSet，之后将task分发到各个Executor上执行



### Spark 通讯架构

Spark通信架构概述

Spark2.x版本使用Netty通讯框架作为内部通讯组件。spark 基于netty新的rpc框架借鉴了Akka的中的设计，它是基于Actor模型，如下图所示

<img src="/Users/wangfulin/github/image/spark/wpsK3ENuB.png" alt="img" style="zoom: 50%;" />