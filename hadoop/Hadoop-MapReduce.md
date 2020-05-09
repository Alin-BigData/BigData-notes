# Hadoop-MapReduce

[toc]

一个分布式运算程序的编程框架

核心功能：

将用户编写的业务逻辑代码和自带默认组件整合成一个完整的分布式计算程序

优点

1．MapReduce 易于编程

它简单的实现一些接口，就可以完成一个分布式程序，这个分布式程序可以分布到大量廉价的PC机器上运行。也就是说你写一个分布式程序，跟写一个简单的串行程序是一模一样的。就是因为这个特点使得MapReduce编程变得非常流行。

![image-20200507172243724](/Users/wangfulin/github/image/hadoop/image-20200507172243724.png)

![image-20200507172318735](/Users/wangfulin/github/image/hadoop/image-20200507172318735.png)

![image-20200507172335336](/Users/wangfulin/github/image/hadoop/image-20200507172335336.png)

## MapReduce核心思想

![image-20200507172407739](/Users/wangfulin/github/image/hadoop/image-20200507172407739.png)

1）分布式的运算程序往往需要分成至少2个阶段。

2）**第一个阶段的MapTask并发实例，完全并行运行，互不相干。**

3）**第二个阶段的ReduceTask并发实例互不相干**，但是他们的数据依赖于上一个阶段的所有MapTask并发实例的输出。

4）MapReduce编程模型只能包含一个Map阶段和一个Reduce阶段，如果用户的业务逻辑非常复杂，那就只能多个MapReduce程序，串行运行。

总结：分析WordCount数据流走向深入理解MapReduce核心思想。

## 常用数据序列化类型

| **Java**类型 | **Hadoop Writable**类型 |
| ------------ | :---------------------: |
| boolean      |     BooleanWritable     |
| byte         |      ByteWritable       |
| int          |       IntWritable       |
| float        |      FloatWritable      |
| long         |      LongWritable       |
| double       |     DoubleWritable      |
| String       |        **Text**         |
| map          |       MapWritable       |
| array        |      ArrayWritable      |

## MapReduce编程规范

用户编写的程序分成三个部分：Mapper、Reducer和Driver。

**Mapper阶段**

- 用户自定义要的Mapper要继承自己的父类
- Mapper的输入的数据是KV对的形式
- **Mapper中的业务逻辑写在map()方法中**
- **map()方法（MapTask进程）对每一个<K,V>调用一次**

**Reducer阶段**

- 用户自定义的Reducer要继承自己的父类

- Reducer的输入数据类型对应Mapper的输出数据类型，也是KV
- **Reducer的业务逻辑写在reduce()方法中**
- **ReduceTask进程对每一组相同k的<k,v>组调用一次reduce()方法**

**Driver阶段**

相当于YARN集群的客户端，用于提交我们整个程序到YARN集群，提交的是封装了MapReduce程序相关运行参数的job对象



## 案例

### wordcount

mapper

```java
// map阶段
// KEYIN 输入数据的key
// VALUEIN 输入数据的value
// KEYOUT 输出数据的key的类型   atguigu,1   ss,1
// VALUEOUT 输出的数据的value类型
public class WordcountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    Text k = new Text();
    IntWritable v = new IntWritable(1);


    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        System.out.println(key.toString());

        // 1 获取一行
        String line = value.toString();

        // 2 切割单词
        String[] words = line.split(" ");

        // 3 循环写出
        for (String word : words) {

            // atguigu
            k.set(word);

            context.write(k, v);
        }
    }

}
```

reduce

```java
// KEYIN, VALUEIN   map阶段输出的key和value
public class WordcountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    // 累加
    IntWritable v = new IntWritable();

    // 按单词reduce
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values,
                          Context context) throws IOException, InterruptedException {
        int sum = 0;

        // 1 累加求和
        for (IntWritable value : values) {
            sum += value.get();
        }

        v.set(sum);

        // 2 写出 atguigu 2
        context.write(key, v);
    }
}
```

drive

```java
public class WordcountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        String basePath = "/Users/wangfulin/warehouse/mapreduce/com.wangfulin.wordcount/";
        args = new String[]{basePath + "input", basePath + "output"};

        // 1 获取配置信息以及封装任务
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2 设置jar加载路径
        job.setJarByClass(WordcountDriver.class);

        // 3 设置map和reduce类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        // 4 设置map输出
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5 设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6 设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 7 提交
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);
    }
}
```

input

```
atguigu atguigu
ss ss
cls cls
jiao
banzhang
xue
hadoop
```

output

```
atguigu	2
banzhang	1
cls	2
hadoop	1
jiao	1
ss	2
xue	1
```

### 序列化案例实操

FlowBean

```java
public class FlowBean implements Writable {
    private long upFlow; // 上行流量
    private long downFlow; // 下行流量
    private long sumFlow; // 总流量

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    // 序列化方法
    @Override
    public void write(DataOutput out) throws IOException {

        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    // 反序列化方法
    @Override
    public void readFields(DataInput in) throws IOException {
        // 必须要求和序列化方法顺序一致
        upFlow = in.readLong();
        downFlow = in.readLong();
        sumFlow = in.readLong();
    }


    // 计算总流量
    public void set(long upFlow2, long downFlow2) {

        upFlow = upFlow2;
        downFlow = downFlow2;
        sumFlow = upFlow2 + downFlow2;

    }

    // 空参构造， 为了后续反射用
    // 反序列化时，需要反射调用空参构造函数，
    // 所以必须有
    public FlowBean() {
        super();
    }

    public FlowBean(long upFlow, long downFlow) {
        super();
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        sumFlow = upFlow + downFlow;
    }

    @Override
    public String toString() {
        return "FlowBean{" +
                "upFlow=" + upFlow +
                ", downFlow=" + downFlow +
                ", sumFlow=" + sumFlow +
                '}';
    }
}
```

mapper

```java
public class FlowCountMapper extends Mapper<LongWritable, Text,Text,FlowBean> {

    Text k = new Text();
    FlowBean v = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行
        String line = value.toString();

        // 2 切割字段
        String[] fields = line.split("\t");

        // 3 封装对象
        // 取出手机号码
        String phoneNum = fields[1];

        // 取出上行流量和下行流量
        long upFlow = Long.parseLong(fields[fields.length - 3]);
        long downFlow = Long.parseLong(fields[fields.length - 2]);

        k.set(phoneNum);
        v.set(downFlow, upFlow);

        // 4 写出
        context.write(k, v);
    }
}
```

Reduce

```java
public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long sum_upFlow = 0;
        long sum_downFlow = 0;
        // 1 遍历所用bean，将其中的上行流量，下行流量分别累加
        for (FlowBean flowBean : values) {
            sum_upFlow += flowBean.getUpFlow();
            sum_downFlow += flowBean.getDownFlow();
        }

        // 2 封装对象
        FlowBean resultBean = new FlowBean(sum_upFlow, sum_downFlow);

        // 3 写出
        context.write(key, resultBean);
    }
}
```

drive

```java
public class FlowsumDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String basePath = "/Users/wangfulin/warehouse/mapreduce/flowsum/";
        args = new String[]{basePath + "input", basePath + "output"};

        Configuration configuration = new Configuration();
        // 1 获取job对象
        Job job = Job.getInstance(configuration);

        // 2 设置jar的路径
        job.setJarByClass(FlowsumDriver.class);

        // 3 关联mapper和reducer
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        // 4 设置mapper输出的key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        // 5 设置最终输出的key和value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 6 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 7 提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);
    }
}
```



## MapReduce框架原理

### MapTask并行度决定机制

数据块：Block是HDFS物理上把数据分成一块一块。

数据数据切片只是**在逻辑上对输入进行分片**，并**不会在磁盘上将其切分成片进行存储**。

<img src="/Users/wangfulin/github/image/hadoop/image-20200508084448058.png" alt="image-20200508084448058" style="zoom:50%;" />

### Job提交流程源码和切片源码详解

#### Job提交流程源码详解

```java
waitForCompletion()

submit();

// 1建立连接
	connect();	
		// 1）创建提交Job的代理
		new Cluster(getConfiguration());
			// （1）判断是本地yarn还是远程
			initialize(jobTrackAddr, conf); 

// 2 提交job
submitter.submitJobInternal(Job.this, cluster)
	// 1）创建给集群提交数据的Stag路径
	Path jobStagingArea = JobSubmissionFiles.getStagingDir(cluster, conf);

	// 2）获取jobid ，并创建Job路径
	JobID jobId = submitClient.getNewJobID();

	// 3）拷贝jar包到集群
copyAndConfigureFiles(job, submitJobDir);	
	rUploader.uploadFiles(job, jobSubmitDir);

// 4）计算切片，生成切片规划文件
writeSplits(job, submitJobDir);
		maps = writeNewSplits(job, jobSubmitDir);
		input.getSplits(job);

// 5）向Stag路径写XML配置文件
writeConf(conf, submitJobFile);
	conf.writeXml(out);

// 6）提交Job,返回提交状态
status = submitClient.submitJob(jobId, submitJobDir.toString(), job.getCredentials());
```

<img src="/Users/wangfulin/github/image/hadoop/image-20200508085106652.png" alt="image-20200508085106652" style="zoom: 67%;" />

#### FileInputFormat切片机制

**切片机制**

（1）简单地按照文件的内容长度进行切片

（2）切片大小，默认等于Block大小

（3）**切片时不考虑数据集整体，而是逐个针对每一个文件单独切片**

**切片大小设置**

maxsize（切片最大值）：参数如果调得比blockSize小，则会让切片变小，而且就等于配置的这个参数的值。

minsize（切片最小值）：参数调的比blockSize大，则可以让切片变得比blockSize还大。

#### CombineTextInputFormat切片机制

​	框架默认的TextInputFormat切片机制是对任务按文件规划切片，**不管文件多小，都会是一个单独的切片**，都会交给一个MapTask，这样如果有大量小文件，就会产生大量的MapTask，处理效率极其低下。**CombineTextInputFormat用于小文件过多的场景**，它可以将多个小文件从逻辑上规划到一个切片中，这样，多个小文件就可以交给一个MapTask处理。

**切片机制**

生成切片过程包括：虚拟存储过程和切片过程二部分。

<img src="/Users/wangfulin/github/image/hadoop/image-20200508093614022.png" alt="image-20200508093614022" style="zoom:50%;" />

（1）虚拟存储过程：

将输入目录下所有文件大小，**依次和设置的setMaxInputSplitSize值比较**，**如果不大于设置的最大值，逻辑上划分一个块。如果输入文件大于设置的最大值且大于两倍，那么以最大值切割一块；当剩余数据大小超过设置的最大值且不大于最大值2倍，此时将文件均分成2个虚拟存储块（防止出现太小切片）。**

> 例如setMaxInputSplitSize值为4M，输入文件大小为8.02M，则先逻辑上分成一个4M。剩余的大小为4.02M，如果按照4M逻辑划分，就会出现0.02M的小的虚拟存储文件，所以将剩余的4.02M文件切分成（2.01M和2.01M）两个文件。
>

（2）切片过程：

（a）判断虚拟存储的文件大小是否大于setMaxInputSplitSize值，大于等于则单独形成一个切片。

（b）如果不大于则跟下一个虚拟存储文件进行合并，共同形成一个切片。

（c）测试举例：有4个小文件大小分别为1.7M、5.1M、3.4M以及6.8M这四个小文件，则虚拟存储之后形成6个文件块，大小分别为：

1.7M，（2.55M、2.55M），3.4M以及（3.4M、3.4M）

最终会形成3个切片，大小分别为：

（1.7+2.55）M，（2.55+3.4）M，（3.4+3.4）M



### FileInputFormat实现类

FileInputFormat常见的接口实现类包括：TextInputFormat、KeyValueTextInputFormat、NLineInputFormat、CombineTextInputFormat和自定义InputFormat等。

#### TextInputFormat

TextInputFormat是默认的FileInputFormat实现类。按行读取每条记录。**键是存储该行在整个文件中的起始字节偏移量， LongWritable类型。值是这行的内容，不包括任何行终止符**（换行符和回车符），Text类型。

![image-20200508120209724](/Users/wangfulin/github/image/hadoop/image-20200508120209724.png)

#### KeyValueTextInputFormat

每一行均为一条记录，被分隔符分割为key，value。可以通过在驱动类中设置`conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, "\t")`;来设定分隔符。默认分隔符是tab

![image-20200508120454766](/Users/wangfulin/github/image/hadoop/image-20200508120454766.png)

此时的键是每行排在**制表符之前**的Text序列。

##### KeyValueTextInputFormat使用案例

```java
public class KVTextMapper extends Mapper<Text, Text, Text, LongWritable> {
    // 1 设置value
    LongWritable v = new LongWritable(1);

    @Override
    protected void map(Text key, Text value, Context context)
            throws IOException, InterruptedException {

        // banzhang ni hao

        // 2 写出
        context.write(key, v);
    }
}
```

reduce

```java
public class KVTextReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    LongWritable v = new LongWritable();

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0L;
        for (LongWritable value : values) {
            sum += value.get();
        }

        v.set(sum);
        context.write(key, v);
    }
}
```

driver

```java
public class KVTextDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String basePath = "/Users/wangfulin/warehouse/mapreduce/KeyValueTextInputFormat/";
        args = new String[]{basePath + "input", basePath + "output"};

        Configuration configuration = new Configuration();
        // 设置切割符
        configuration.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, " ");

        DeleteOutput.delete(args[1], configuration);

        // 1 获取job对象
        Job job = Job.getInstance(configuration);

        // 2 设置jar存储路径
        job.setJarByClass(KVTextDriver.class);

        // 3 关联mapper和reducer类
        job.setMapperClass(KVTextMapper.class);
        job.setReducerClass(KVTextReducer.class);

        // 4 设置mapper输出的key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 5 设置最终输出的key和value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 设置输入的方式
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        // 6 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 7 提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);
    }
}
```

#### NLineInputFormat

如果使用NlineInputFormat，代表每个map进程处理的InputSplit**不再按Block块去划分**，而是**按NlineInputFormat指定的行数N来划分**。即输入文件的总行数/N=切片数，如果不整除，切片数=商+1。

![image-20200508120638516](/Users/wangfulin/github/image/hadoop/image-20200508120638516.png)

这里的键和值与TextInputFormat生成的一样。**键是存储该行在整个文件中的起始字节偏移量， LongWritable类型。值是这行的内容，不包括任何行终止符**（换行符和回车符），Text类型。

##### NLineInputFormat使用案例

mapper

```java
public class NLineMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    private Text k = new Text();
    private LongWritable v = new LongWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行
        String line = value.toString();

        // 2 切割
        String[] splited = line.split(" ");

        // 3 循环写出
        for (int i = 0; i < splited.length; i++) {

            k.set(splited[i]);

            context.write(k, v);
        }
    }
}
```

reduce	

```java
public class NLineReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
    LongWritable v = new LongWritable();

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        // 1 汇总
        for (LongWritable value : values) {
            sum += value.get();
        }

        v.set(sum);

        // 2 输出
        context.write(key, v);
    }
}
```

```java
public class NLineDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String basePath = "/Users/wangfulin/warehouse/mapreduce/NLineInputFormat/";
        args = new String[]{basePath + "input", basePath + "output"};

        Configuration configuration = new Configuration();
        DeleteOutput.delete(args[1], configuration);
        // 1 获取job对象

        Job job = Job.getInstance(configuration);

        // 7设置每个切片InputSplit中划分三条记录
        NLineInputFormat.setNumLinesPerSplit(job, 3);

        // 8使用NLineInputFormat处理记录数
        job.setInputFormatClass(NLineInputFormat.class);

        // 2设置jar包位置，关联mapper和reducer
        job.setJarByClass(NLineDriver.class);
        job.setMapperClass(NLineMapper.class);
        job.setReducerClass(NLineReducer.class);

        // 3设置map输出kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        // 4设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        // 5设置输入输出数据路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 6提交job
        job.waitForCompletion(true);
    }
}
```

#### 自定义InputFormat

**自定义InputFormat步骤如下**：

（1）自定义一个类继承FileInputFormat。

（2）改写RecordReader，实现一次读取一个完整文件封装为KV。

（3）在输出时使用SequenceFileOutPutFormat输出合并文件。

（3）在输出时使用SequenceFileOutPutFormat输出合并文件。

**需求**

​	将多个小文件合并成一个SequenceFile文件（SequenceFile文件是Hadoop用来存储二进制形式的key-value对的文件格式），SequenceFile里面存储着多个文件，存储的形式为文件路径+名称为key，文件内容为value。

![image-20200508141852016](/Users/wangfulin/github/image/hadoop/image-20200508141852016.png)

- [ ] 

- [ ] 插入代码连接



### MapReduce工作流程

#### 流程示意图

![image-20200509102539762](/Users/wangfulin/github/image/hadoop/image-20200509102539762.png)

**mapper阶段：**

1 待处理文件；

2 客户端提交文件信息，形成任务分配规划（即切片信息）；

3 提交切片信息（切片信息，jar信息，job.xml）

4 Yarn调用Resource Manager计算出MapTask数量; 根据切片信息启动MapTask

5 读取数据（怎么读，由RecordReader决定）

6 读完以后，返回给mapper中的map，处理业务逻辑。结果写出到outputCollector收集器

7 收集器向环形缓冲区写入<k,V>，默认100M，向两侧写，左侧写数据的索引，右侧写真实数据 。写到百分之八十之后，进行一次溢写，将内存中的数据写入磁盘，之后反向。

8 写入磁盘前，分区，排序。排序按照字典排序。（快排）

9 溢写到文件

10  相同分区内的数据进行merge归并排序

![image-20200509102705846](/Users/wangfulin/github/image/hadoop/image-20200509102705846.png)

**Reduce阶段：**

11 合并

12 等待所有的mapperTask全部完成，由MrappMaster决定，多少个分区启动相应数量的ReducerTask，并告知处理数据的范围（数据分区）

13 拷贝数据到ReduceTask中，如果数据量不大，则会将数据存内存中，否则会存磁盘。并合并文件（归并排序），对数据进行分组

14 一次读取一组，按照key相同原则，放到reduce中，处理业务逻辑。

16 写出

### Shuffle机制

Map方法之后，Reduce方法之前的数据处理过程称之为Shuffle

![image-20200509115337439](/Users/wangfulin/github/image/hadoop/image-20200509115337439.png)

**具体Shuffle过程详解，如下：**

1）MapTask收集我们的map()方法输出的kv对，放到内存缓冲区中

2）从内存缓冲区不断溢出本地磁盘文件，可能会溢出多个文件

3）多个溢出文件会被合并成大的溢出文件

4）在溢出过程及合并的过程中，都要调用Partitioner进行分区和针对key进行排序

5）ReduceTask根据**自己的分区号，去各个MapTask机器上取相应的结果分区数据**

6）ReduceTask会取到同一个分区的来自不同MapTask的结果文件，ReduceTask会将这些文件再进行合并（归并排序）

7）合并成大文件后，Shuffle的过程也就结束了，后面进入ReduceTask的逻辑运算过程（从文件中取出一个一个的键值对Group，调用用户自定义的reduce()方法）

**注意：**

Shuffle中的缓冲区大小会影响到MapReduce程序的执行效率，原则上说，缓冲区越大，磁盘io的次数越少，执行速度就越快。

缓冲区的大小可以通过参数调整，参数：io.sort.mb默认100M。

#### Partition分区

要求将统计结果按照条件输出到不同文件中（分区）。比如：将统计结果按照手机归属地不同省份输出到不同文件中（分区）

```
public class HashPartitioner<K, V> extends Partitioner<K, V> {

  public int getPartition(K key, V value, int numReduceTasks) {
    return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
  }

}
```

默认分区是根据key的hashCode对ReduceTasks个数取模得到的。用户没法控制哪个key存储到哪个分区。

**自定义Partitioner步骤：**

![image-20200509131053641](/Users/wangfulin/github/image/hadoop/image-20200509131053641.png)

**分区总结**

（1）如果ReduceTask的数量> getPartition的结果数，则会多产生几个空的输出文件part-r-000xx；

（2）如果1<ReduceTask的数量<getPartition的结果数，则有一部分分区数据无处安放，会Exception；

（3）如果ReduceTask的数量=1，则不管MapTask端输出多少个分区文件，最终结果都交给这一个ReduceTask，最终也就只会产生一个结果文件 part-r-00000；

（4）**分区号必须从零开始，逐一累加。**

![image-20200509131251200](/Users/wangfulin/github/image/hadoop/image-20200509131251200.png)



#### WritableComparable排序

​    MapTask和ReduceTask均会对数据按照key进行排序。该操作属于Hadoop的默认行为。任何应用程序中的数据均会被排序，而不管逻辑上是否需要。

默认排序是**按照字典顺序排序**，且实现该排序的方法是**快速排序**。

 	对于MapTask，它会将处理的结果暂时放到**环形缓冲区**中，当环形缓冲区使用率达到一定阈值后，再对缓冲区中的数据**进行一次快速排序**，并将这些有序数据溢写到磁盘上，而**当数据处理完毕后，它会对磁盘上所有文件进行归并排序**。

​	对于ReduceTask，它从每个MapTask上远程拷贝相应的数据文件，如果**文件大小超过一定阈值，则溢写磁盘上**，否则存储在内存中。如果磁盘上文件数目达到一定阈值，则进行一次归并排序以生成一个更大文件；如果内存中文件大小或者数目超过一定阈值，则进行一次合并后将数据溢写到磁盘上。当所有数据拷贝完毕后，ReduceTask统一对内存和磁盘上的所有数据进行一次归并排序。



**排序的分类**

![image-20200509132924185](/Users/wangfulin/github/image/hadoop/image-20200509132924185.png)

**自定义排序WritableComparable**

原理分析

bean对象做为key传输，**需要实现WritableComparable接口重写compareTo方法，就可以实现排序。**

```java
@Override
public int compareTo(FlowBean o) {

	int result;
		
	// 按照总流量大小，倒序排列
	if (sumFlow > bean.getSumFlow()) {
		result = -1;
	}else if (sumFlow < bean.getSumFlow()) {
		result = 1;
	}else {
		result = 0;
	}

	return result;
}
```

#### WritableComparable排序案例（全排序）

​		最终输出结果只有一个文件，且文件内部有序。实现方式是**只设置一个ReduceTask**。但该方法在处理大型文件时效率极低，因为一台机器处理所有文件，完全丧失了MapReduce所提供的并行架构。

FlowBean

```java
public class FlowBean implements WritableComparable<FlowBean> {

    private long upFlow; // 上行流量
    private long downFlow; // 下行流量
    private long sumFlow;  // 总流量

    // 反序列化时，需要反射调用空参构造函数，所以必须有
    public FlowBean() {
        super();
    }

    public FlowBean(long upFlow, long downFlow) {
        super();
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        sumFlow = upFlow + downFlow;
    }


    //  按照总流量大小，倒序排列
    @Override
    public int compareTo(FlowBean bean) {
        int result;

        // 核心比较条件判断
        if (sumFlow > bean.getSumFlow()) {
            result = -1;
        } else if (sumFlow < bean.getSumFlow()) {
            result = 1;
        } else {
            result = 0;
        }

        return result;
    }


    // 序列化
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    // 反序列化
    @Override
    public void readFields(DataInput in) throws IOException {
        upFlow = in.readLong();
        downFlow = in.readLong();
        sumFlow = in.readLong();
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    // 计算总流量
    public void set(long upFlow2, long downFlow2) {

        upFlow = upFlow2;
        downFlow = downFlow2;
        sumFlow = upFlow2 + downFlow2;

    }

    @Override
    public String toString() {
        return "FlowBean{" +
                "upFlow=" + upFlow +
                ", downFlow=" + downFlow +
                ", sumFlow=" + sumFlow +
                '}';
    }
}
```

- [ ] 插入代码

#### WritableComparable排序案例实操（区内排序）

- [ ] 插入代码



### Combiner合并

![image-20200509163806527](/Users/wangfulin/github/image/hadoop/image-20200509163806527.png)

**自定义Combiner实现步骤**

（a）自定义一个Combiner继承Reducer，重写Reduce方法

```java
public class WordcountCombiner extends Reducer<Text, IntWritable, Text,IntWritable>{

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {

        // 1 汇总操作
		int count = 0;
		for(IntWritable v :values){
			count += v.get();
		}

        // 2 写出
		context.write(key, new IntWritable(count));
	}
}
```

（b）在Job驱动类中设置： 

```java
job.setCombinerClass(WordcountCombiner.class);
```



**GroupingComparator分组（辅助排序）**

对Reduce阶段的数据根据某一个或几个字段进行分组。

分组排序步骤：

（1）自定义类继承WritableComparator

（2）重写compare()方法

```java
@Override
public int compare(WritableComparable a, WritableComparable b) {
		// 比较的业务逻辑
		return result;
}
```

（3）创建一个构造将比较对象的类传给父类

```java
protected OrderGroupingComparator() {
		super(OrderBean.class, true);
}
```

- [ ] 代码链接



### MapTask工作机制

![image-20200509234054144](/Users/wangfulin/github/image/hadoop/image-20200509234054144.png)

​		1）Read阶段：MapTask通过用户编写的RecordReader，从输入InputSplit中解析出一个个key/value。

​	（2）Map阶段：该节点主要是将解析出的key/value交给用户编写map()函数处理，并产生一系列新的key/value。

​	（3）Collect收集阶段：在用户编写map()函数中，当数据处理完成后，一般会调用OutputCollector.collect()输出结果（收集器）。在该函数内部，它会将生成的key/value分区（调用Partitioner），并**写入一个环形内存缓冲区中**。

​	（4）Spill阶段：即“溢写”，当环形缓冲区满后，MapReduce会将数据写到本地磁盘上，**生成一个临时文件**。需要注意的是，**将数据写入本地磁盘之前，先要对数据进行一次本地排序，并在必要时对数据进行合并、压缩等操作。**

​	溢写阶段详情：

​	步骤1：利用**快速排序算法**对缓存区内的数据进行排序，排序方式是，先按照分区编号Partition进行排序，然后按照key进行排序。这样，经过排序后，数据**以分区为单位聚集在一起**，**且同一分区内所有数据按照key有序。**

​	步骤2：按照分区编号由小到大依次将每个分区中的数据**写入任务工作目录下的临时文件output/spillN.out**（N表示当前溢写次数）中。如果用户设置了Combiner，则写入文件之前，对每个分区中的数据进行一次聚集操作。

​	步骤3：**将分区数据的元信息写到内存索引数据结构SpillRecord中**，其中每个分区的元信息包括在临时文件中的偏移量、压缩前数据大小和压缩后数据大小。如果当前内存索引大小超过1MB，则将内存索引写到文件output/spillN.out.index中。

​	（5）Combine阶段：当所有数据处理完成后，**MapTask对所有临时文件进行一次合并，以确保最终只会生成一个数据文件**。

​	当所有数据处理完后，MapTask会将所有临时文件合并成一个大文件，并保存到文件output/file.out中，同时生成相应的索引文件output/file.out.index。

​	在进行文件合并过程中，MapTask以分区为单位进行合并。**对于某个分区，它将采用多轮递归合并的方式。**每轮合并io.sort.factor（默认10）个文件，并将产生的文件重新加入待合并列表中，对文件排序后，重复以上过程，直到最终得到一个大文件。

​	让每个MapTask最终只生成一个数据文件，可避免同时打开大量文件和同时读取大量小文件产生的随机读取带来的开销。

### ReduceTask工作机制

![image-20200509234721093](/Users/wangfulin/github/image/hadoop/image-20200509234721093.png)

（1）Copy阶段：ReduceTask从各个MapTask上远程拷贝一片数据，并针对某一片数据，如果其大小超过一定阈值，则写到磁盘上，否则直接放到内存中。

​	（2）Merge阶段：在远程拷贝数据的同时，ReduceTask启动了两个后台线程对内存和磁盘上的文件进行合并，以防止内存使用过多或磁盘上文件过多。

​	（3）Sort阶段：按照MapReduce语义，用户编写reduce()函数输入数据是按key进行聚集的一组数据。为了将key相同的数据聚在一起，Hadoop采用了基于排序的策略。由于各个MapTask已经实现对自己的处理结果进行了局部排序，因此，ReduceTask只需对所有数据进行一次归并排序即可。

​	（4）Reduce阶段：reduce()函数将计算结果写到HDFS上。