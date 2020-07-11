# Flink进阶

### Flink 常见核心概念分析

Flink 这个框架中的分布式缓存、重启策略、并行度等

#### 分布式缓存

使用分布式缓存有两个步骤。

- 第一步：首先需要在 env 环境中注册一个文件，该文件可以来源于本地，也可以来源于 HDFS ，并且为该文件取一个名字。

- 第二步：在使用分布式缓存时，可根据注册的名字直接获取。

```scala
public static void main(String[] args) throws Exception {

final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
   env.registerCachedFile("/Users/wangzhiwu/WorkSpace/quickstart/distributedcache.txt", "distributedCache");
       //1：注册一个文件,可以使用hdfs上的文件 也可以是本地文件进行测试
       DataSource<String> data = env.fromElements("Linea", "Lineb", "Linec", "Lined");

       DataSet<String> result = data.map(new RichMapFunction<String, String>() {
           private ArrayList<String> dataList = new ArrayList<String>();

           @Override
           public void open(Configuration parameters) throws Exception {
               super.open(parameters);
               //2：使用该缓存文件
               File myFile = getRuntimeContext().getDistributedCache().getFile("distributedCache");
               List<String> lines = FileUtils.readLines(myFile);
               for (String line : lines) {
                   this.dataList.add(line);
                   System.err.println("分布式缓存为:" + line);
               }
           }

           @Override
           public String map(String value) throws Exception {
               //在这里就可以使用dataList
               System.err.println("使用datalist：" + dataList + "-------" +value);
               //业务逻辑
               return dataList +"：" +  value;
           }
       });

       result.printToErr();
   }

```

注意：

- 缓存的文件在任务运行期间最好是只读状态，否则容易造成数据一致性问题。

- 缓存数据不宜过大，会影响Task的执行速度。

#### 故障恢复和重启策略

Flink 支持了不同级别的故障恢复策略，jobmanager.execution.failover-strategy 的可配置项有两种：full 和 region。

当配置的故障恢复策略为 full 时，集群中的 Task 发生故障，那么该任务的所有 Task 都会发生重启。

Flink 基于 Region 的局部重启策略。在这个策略下，Flink 会把任务分成不同的 Region，当某一个 Task 发生故障时，Flink 会计算需要故障恢复的最小 Region。

重启Region的判断逻辑：

- 发生错误的Task所在的Region需要重启；
- 数据依赖部分损坏或丢失，产生数据的Region重启；
- 下游Region重启。

#### 重启策略

常用的重启策略包括：

- 固定延迟重启策略模式

- 失败率重启策略模式

- 无重启策略模式

如果用户配置了 **checkpoint**，但没有设置重启策略，那么会按照固定延迟重启策略模式进行重启；如果用户没有配置 checkpoint，那么默认不会重启。

##### 无重启策略模式

flink-conf.yaml 中配置：

```
restart-strategy: none
```

或

```scala
final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setRestartStrategy(RestartStrategies.noRestart());
```

##### 固定延迟重启策略模式

flink-conf.yaml 中设置：

```
restart-strategy: fixed-delay
```

有两个参数，分别是重试次数和重试的间隔时间

```
restart-strategy.fixed-delay.attempts: 3
restart-strategy.fixed-delay.delay: 5 s
```

或代码：

```scala
env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
        3, // 重启次数
        Time.of(5, TimeUnit.SECONDS) // 时间间隔
));
```

##### 失败率重启策略模式

flink-conf.yaml 中配置：

```
restart-strategy: failure-rate
```

失败率重启策略在 Job 失败后会重启，但是超过失败率后，Job 会最终被认定失败。在两个连续的重启尝试之间，重启策略会等待一个固定的时间。

例：5 分钟内若失败了 3 次，则认为该任务失败，每次失败的重试间隔为 5 秒。

```
restart-strategy.failure-rate.max-failures-per-interval: 3
restart-strategy.failure-rate.failure-rate-interval: 5 min
restart-strategy.failure-rate.delay: 5 s
```

或代码：

```scala
env.setRestartStrategy(RestartStrategies.failureRateRestart(
        3, // 每个时间间隔的最大故障次数
        Time.of(5, TimeUnit.MINUTES), // 测量故障率的时间间隔
        Time.of(5, TimeUnit.SECONDS) //  每次任务失败时间间隔
));
```

推荐在代码中指定每个任务的重试机制和重启策略。

#### 并行度

提高任务的并行度（Parallelism）在很大程度上可以大大提高任务运行速度。

四种级别来设置任务的并行度。

- 算子级别

在代码中可以调用 setParallelism 方法来设置每一个算子的并行度。

```scala
DataSet<Tuple2<String, Integer>> counts =
      text.flatMap(new LineSplitter())
            .groupBy(0)
            .sum(1).setParallelism(1);
```

- 执行环境级别

对当前任务的所有算子、Source、Sink 生效。可在算子级别覆盖。

```scala
final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setParallelism(5);
```

- 提交任务级别

加上 -p

```
./bin/flink run -p 10 WordCount.jar
```

- 系统配置级别

 flink-conf.yaml 中的一个配置：parallelism.default

**优先级**：算子级别 > 执行环境级别 > 提交任务级别 > 系统配置级别。

