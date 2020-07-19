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



### Flink 窗口、时间和水印Flink 

#### Flink 的窗口和时间

窗口分为：

- 滚动窗口，窗口数据**有固定的大小**，窗口中的**数据不会叠加**；
- 滑动窗口，窗口数据**有固定的大小**，并且**有生成间隔**；
- 会话窗口，窗口数据**没有固定的大小**，根据用户传入的参数进行划分，窗口数据无叠加。

时间分为：

- 事件时间（Event Time），即事件实际发生的时间；
- 摄入时间（Ingestion Time），事件进入流处理框架的时间；
- 处理时间（Processing Time），事件被处理的时间。

![image (18).png](/Users/wangfulin/github/github-note/images/flink/CgqCHl65D6SAKNl-AADISYD73gQ276.png)

```scala
final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//设置时间属性为 EventTime
env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

DataStream<MyEvent> stream = env.addSource(new FlinkKafkaConsumer09<MyEvent>(topic, schema, props));

stream
    .keyBy( (event) -> event.getUser() )
    .timeWindow(Time.hours(1))
    .reduce( (a, b) -> a.add(b) )
    .addSink(...);
```

**事件时间（Event Time）**

Flink 注册 EventTime 是通过 InternalTimerServiceImpl.registerEventTimeTimer 来实现的。该方法有两个入参：namespace 和 time，其中 time 是触发定时器的时间，namespace 则被构造成为一个 TimerHeapInternalTimer 对象，然后将其放入 KeyGroupedInternalPriorityQueue 队列中。

**处理时间（Processing Time）**

```scala
final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
```

**摄入时间（Ingestion Time）**

理论上 Ingestion Time 处于 Event Time 和 Processing Time之间。

#### 水印（WaterMark）

它在本质上是一个时间戳。

- EventTime 每条数据都携带时间戳；

- ProcessingTime 数据不携带任何时间戳的信息；

- IngestionTime 和 EventTime 类似，不同的是 Flink 会使用系统时间作为时间戳绑定到每条数据，可以防止 Flink 内部处理数据是发生乱序的情况，但无法解决数据到达 Flink 之前发生的乱序问题。

所以，我们在处理消息乱序的情况时，会用 EventTime 和 WaterMark 进行配合使用。在程序并行度大于 1 的情况下，会有多个流产生水印和窗口，这时候 Flink 会选取时间戳最小的水印。

**水印是如何生成的**

Flink 提供了 assignTimestampsAndWatermarks() 方法来实现水印的提取和指定，该方法接受的入参有 AssignerWithPeriodicWatermarks 和 AssignerWithPunctuatedWatermarks 两种。

![image (25).png](/Users/wangfulin/github/github-note/images/flink/Ciqc1F65D-2AKmZ2AAITdhcoNis465.png)

**水印种类**

**周期性水印**

我们在使用 AssignerWithPeriodicWatermarks 周期生成水印时，周期默认的时间是 200ms，这个时间的指定位置为：

```scala
@PublicEvolving
public void setStreamTimeCharacteristic(TimeCharacteristic characteristic) {
    this.timeCharacteristic = Preconditions.checkNotNull(characteristic);
    if (characteristic == TimeCharacteristic.ProcessingTime) {
        getConfig().setAutoWatermarkInterval(0);
    } else {
        getConfig().setAutoWatermarkInterval(200);
    }
}
```

Flink 在这里提供了 3 种提取 EventTime() 的方法，分别是：

- AscendingTimestampExtractor

- BoundedOutOfOrdernessTimestampExtractor

- IngestionTimeExtractor

这三种方法中 BoundedOutOfOrdernessTimestampExtractor() 用的最多，需特别注意，在这个方法中的 maxOutOfOrderness 参数，该参数指的是允许数据乱序的时间范围。简单说，这种方式允许数据迟到 maxOutOfOrderness 这么长的时间。

**PunctuatedWatermark 水印**

这种水印的生成方式 Flink 没有提供内置实现，它适用于根据接收到的消息判断是否需要产生水印的情况，用这种水印生成的方式并不多见。