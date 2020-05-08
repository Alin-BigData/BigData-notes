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

将输入目录下所有文件大小，**依次和设置的setMaxInputSplitSize值比较**，**如果不大于设置的最大值，逻辑上划分一个块。如果输入文件大于设置的最大值且大于两倍，那么以最大值切割一块；==当剩余数据大小超过设置的最大值且不大于最大值2倍，此时将文件均分成2个虚拟存储块（防止出现太小切片）。==**

> 例如setMaxInputSplitSize值为4M，输入文件大小为8.02M，则先逻辑上分成一个4M。剩余的大小为4.02M，如果按照4M逻辑划分，就会出现0.02M的小的虚拟存储文件，所以将剩余的4.02M文件切分成（2.01M和2.01M）两个文件。
>

（2）切片过程：

（a）判断虚拟存储的文件大小是否大于setMaxInputSplitSize值，大于等于则单独形成一个切片。

（b）如果不大于则跟下一个虚拟存储文件进行合并，共同形成一个切片。

（c）测试举例：有4个小文件大小分别为1.7M、5.1M、3.4M以及6.8M这四个小文件，则虚拟存储之后形成6个文件块，大小分别为：

1.7M，（2.55M、2.55M），3.4M以及（3.4M、3.4M）

最终会形成3个切片，大小分别为：

（1.7+2.55）M，（2.55+3.4）M，（3.4+3.4）M