# Apache Flink 介绍

来源：[Flink 从 0 到 1 学习 —— Apache Flink 介绍](http://www.54tianzhisheng.cn/2018/10/13/flink-introduction/)

### 数据集类型有哪些呢：

- 无穷数据集：无穷的持续集成的数据集合
- 有界数据集：有限不会改变的数据集合

数据运算模型有哪些呢：

- 流式：只要数据一直在产生，计算就持续地进行
- 批处理：在预先定义的时间内运行计算，当完成时释放计算机资源

运用于：

批处理 --历史数据结果集

流计算 -- 处理实时数据流

事件驱动应用 --监控事件的服务



Flink的基石：

- Checkpoint
- State
- Time
- Window

![image-20200514093218995](/Users/wangfulin/github/image/flink/image-20200514093218995.png)

#### 抽象级别

![image-20200514093317245](/Users/wangfulin/github/image/flink/image-20200514093317245.png)

**最底层提供了有状态流：**它将通过过程函数（Process Function）嵌入到 DataStream API 中。它允许用户可以自由地处理来自一个或多个流数据的事件，并使用一致、容错的状态。除此之外，用户可以注册事件时间和处理事件回调，从而使程序可以实现复杂的计算。



**DataStream / DataSet API：** 是 Flink 提供的核心 API ，**DataSet 处理有界的数据集，DataStream 处理有界或者无界的数据流**。用户可以通过各种方法（map / flatmap / window / keyby / sum / max / min / avg / join 等）将数据进行转换 / 计算。



**Table API** 是以表为中心的声明式 DSL，其中表可能会动态变化（在表达流数据时）。Table API提供了例如 select、project、join、group-by、aggregate 等操作，使用起来却更加简洁（代码量更少）。



Flink 提供的最高层级的抽象是 **SQL** 。这一层抽象在语法与表达能力上与 *Table API* 类似，但是是以 SQL查询表达式的形式表现程序。SQL 抽象与 Table API 交互密切，同时 SQL 查询可以直接在 Table API 定义的表上执行。

### Flink 程序与数据流结构

![image-20200514100411555](/Users/wangfulin/github/image/flink/image-20200514100411555.png)

Flink 应用程序结构就是如上图所示：

1、**Source: 数据源**：Flink 在流处理和批处理上的 source 大概有 4 类：

- 基于本地集合的 source；

- 基于文件的 source；

- 基于网络套接字的 source；

- 自定义的 source。

  自定义的 source 常见的有 Apache kafka、Amazon Kinesis Streams、RabbitMQ、Twitter Streaming API、Apache NiFi 等，当然你也可以定义自己的 source。

2、**Transformation**：数据转换的各种操作，有 Map / FlatMap / Filter / KeyBy / Reduce / Fold / Aggregations / Window / WindowAll / Union / Window join / Split / Select / Project 等，操作很多，可以将数据转换计算成你想要的数据。

3、**Sink**：接收器，**Flink 将转换计算后的数据发送的地点** ，你可能需要存储下来，Flink 常见的 Sink 大概有如下几类：写入文件、打印出来、写入 socket 、自定义的 sink 。自定义的 sink 常见的有 Apache kafka、RabbitMQ、MySQL、ElasticSearch、Apache Cassandra、Hadoop FileSystem 等，同理你也可以定义自己的 sink。

Flink 保存点提供了一个**状态化**的版本机制，**使得能以无丢失状态和最短停机时间的方式更新应用或者回退历史数据。**

![image-20200514103659465](/Users/wangfulin/github/image/flink/image-20200514103659465.png)

Flink 的程序内在是并行和分布式的，数据流可以被分区成 **stream partitions**，operators 被划分为**operator subtasks**; 这些 subtasks **在不同的机器或容器中分不同的线程独立运行**；operator subtasks 的数量在具体的 operator 就是并行计算数，**程序不同的 operator 阶段可能有不同的并行数**；如下图所示，source operator 的并行数为 2，但最后的 sink operator 为1；

![image-20200514103812763](/Users/wangfulin/github/image/flink/image-20200514103812763.png)

自己的内存管理

**Flink 在 JVM 中提供了自己的内存管理，使其独立于 Java 的默认垃圾收集器。 它通过使用散列，索引，缓存和排序有效地进行内存管理。**

### flink 作业提交架构流程可见下图：

![image-20200516195908757](/Users/wangfulin/github/image/flink/image-20200516195908757.png)

1、Program Code：我们编写的 Flink 应用程序代码

2、Job Client：Job Client 不是 Flink 程序执行的内部部分，但**它是任务执行的起点**。 **Job Client 负责接受用户的程序代码，然后创建数据流，将数据流提交给 Job Manager 以便进一步执行。 执行完成后，Job Client 将结果返回给用户。**

![image-20200516200243873](/Users/wangfulin/github/image/flink/image-20200516200243873.png)

3、Job Manager：主进程（也称为作业管理器）协调和管理程序的执行。 它的**主要职责包括安排任务**，**管理checkpoint ，故障恢复等**。机器集群中至少要有一个 master，master 负责调度 task，协调 checkpoints 和容灾，高可用设置的话可以有多个 master，但要保证一个是 leader, 其他是 standby; Job Manager 包含 Actor system、Scheduler、Check pointing 三个重要的组件

4、Task Manager：从 Job Manager 处接收需要部署的 Task。**Task Manager 是在 JVM 中的一个或多个线程中执行任务的工作节点**。 任务执行的并行性由每个 Task Manager 上可用的任务槽决定。 每个任务代表分配给任务槽的一组资源。 例如，如果 Task Manager 有四个插槽，那么它将为每个插槽分配 25％ 的内存。 可以在任务槽中运行一个或多个线程。**同一插槽中的线程共享相同的 JVM。** 同一 JVM 中的任务共享 TCP 连接和心跳消息。Task Manager 的一个Slot代表一个可用线程，该线程具有固定的内存，注意 **Slot 只对内存隔离**，没有对 CPU 隔离。默认情况下，Flink 允许子任务共享 Slot，即使它们是不同 task 的 subtask，只要它们来自相同的 job。这种共享可以有更好的资源利用率。

