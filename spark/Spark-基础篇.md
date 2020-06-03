# Spark-基础篇

[toc]

## wordCount执行过程

数据流分析：

```
textFile("input")：读取本地文件input文件夹数据；一行一行读

flatMap(_.split(" "))：压平操作，按照空格分割符将一行数据映射成一个个单词；

map((_,1))：对每一个元素操作，将单词映射为元组；

reduceByKey(_+_)：按照key将值进行聚合，相加；

collect：将数据收集到Driver端展示。
```

![image-20200525181941524](/Users/wangfulin/github/image/spark/image-20200525181941524.png)

![image-20200526000357406](/Users/wangfulin/github/image/spark/image-20200526000357406.png)





![image-20200526000330985](/Users/wangfulin/github/image/spark/image-20200526000330985.png)

## RDD

![image-20200526134204696](/Users/wangfulin/github/image/spark/image-20200526134204696.png)

RDD将数据处理的逻辑进行封装

![image-20200526134243720](/Users/wangfulin/github/image/spark/image-20200526134243720.png)

RDD（Resilient Distributed Dataset）叫做弹性分布式数据集，是Spark中最基本的数据抽象。代码中是一个抽象类，它代表一个不可变、可分区、里面的元素可并行计算的集合。

移动不如计算：优先位置，将计算移动到数据区。



### RDD的属性

1) 一组分区（Partition），即数据集的基本组成单位;

2) 一个计算每个分区的函数;

3) RDD之间的依赖关系;

4) 一个Partitioner，即RDD的分片函数;

5) 一个列表，存储存取每个Partition的优先位置（preferred location）。

![image-20200602094139651](/Users/wangfulin/github/image/spark/image-20200602094139651.png)

输入可能以多个文件的形式存储在HDFS上，每个File都包含了很多块，称为Block。当Spark读取这些文件作为输入时，会根据具体数据格式对应的InputFormat进行解析，一般是**将若干个Block合并成一个输入分片，称为InputSplit**，**注意**InputSplit不能跨越文件。随后将为这些输入分片生成具体的Task。InputSplit与Task是一一对应的关系。随后这些具体的Task每个都会被分配到集群上的某个节点的某个Executor去执行。

1) 每个节点可以起一个或多个Executor。

2) 每个Executor由若干core组成，每个Executor的每个core一次只能执行一个Task。

3) 每个Task执行的结果就是生成了目标RDD的一个partiton。

注意: 这里的core是虚拟的core而不是机器的物理CPU核，可以理解为就是Executor的一个工作线程。而 **Task被执行的并发度 = Executor数目 * 每个Executor核数**。至于partition的数目：

1) 对于数据读入阶段，例如sc.textFile，输入文件被划分为多少InputSplit就会需要多少初始Task。

2) 在Map阶段partition数目保持不变。

3) 在Reduce阶段，RDD的聚合会触发shuffle操作，聚合后的RDD的partition数目跟具体操作有关，例如repartition操作会聚合成指定分区数，还有一些算子是可配置的。

RDD在计算的时候，每个分区都会起一个task，所以**rdd的分区数目决定了总的的task数目**。申请的计算节点（Executor）数目和每个计算节点核数，决定了你同一时刻可以并行执行的task。

比如的RDD有100个分区，那么计算的时候就会生成100个task，你的资源配置为10个计算节点，每个两2个核，同一时刻可以并行的task数目为20，计算这个RDD就需要5个轮次。如果计算资源不变，你有101个task的话，就需要6个轮次，在最后一轮中，只有一个task在执行，其余核都在空转。如果资源不变，你的RDD只有2个分区，那么同一时刻只有2个task运行，其余18个核空转，造成资源浪费。**这就是在spark调优中，增大RDD分区数目，增大任务并行度的做法。**

 

### RDD的创建

​	在Spark中创建RDD的创建方式可以分为三种：**从集合中创建RDD；从外部存储创建RDD；从其他RDD创建。**

#### 从集合中创建

从集合中创建RDD，spark主要提供了两种函数：parallelize和makeRDD

1）使用parallelize()从集合创建

```scala
scala> val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8))

rdd: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[0] at parallelize at <console>:24
```

2）使用makeRDD()从集合创建

```scala
scala> val rdd1 = sc.makeRDD(Array(1,2,3,4,5,6,7,8))

rdd1: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[1] at makeRDD at <console>:24
```

makeRDD底层使用的是parallelize



#### 由外部存储系统的数据集创建

比如hdfs、hbase、本地文件等

如：

```scala
scala> val rdd2= sc.textFile("hdfs://hadoop102:9000/RELEASE")
rdd2: org.apache.spark.rdd.RDD[String] = hdfs:// hadoop102:9000/RELEASE MapPartitionsRDD[4] at textFile at <console>:24
```

读取文件时，传递的分区参数为最小分区数，但是不一定是这个分区数，取决于hadoop读取文件时，分片规则



### RDD的转换

RDD整体上分为Value类型和Key-Value类型。

所有算子的计算功能都是由Executor执行。

例如：

```scala
		val i = 10
    val mapRdd = listRdd.map(_ * i)
```

这里的i是在Driver中

_*i的计算在Executor中，需要在网络中传输这个i，因此需要考虑序列化，否则不能传输过去。

![image-20200527083153908](/Users/wangfulin/github/image/spark/image-20200527083153908.png)



#### Value类型

##### map(func)案例

```scala
    // map算子
    val listRdd = sc.makeRDD(1 to 10)

    val mapRdd = listRdd.map(_ * 2)

    mapRdd.collect().foreach(println)
```

![image-20200527073028004](/Users/wangfulin/github/image/spark/image-20200527073028004.png)

```scala
    // 可以对一个RDD中的所有分区进行遍历
    // 效率高于map算子，减少发送到执行器的交互次数
    // 但是可能因为内存问题，造成OOM
    // 假设有N个元素，有M个分区，那么map的函数的将被调用N次,
    // 而mapPartitions被调用M次,一个函数一次处理所有分区。
    val mapPartitionsRdd = listRdd.mapPartitions(datas => {
      datas.map(_ * 2)
    })

    mapPartitionsRdd.collect().foreach(println)
```

![image-20200527073045270](/Users/wangfulin/github/image/spark/image-20200527073045270.png)

```scala
    // mapPartitionsWithIndex带分区号
    val tupleRDD = listRdd.mapPartitionsWithIndex((index, items) => {
      items.map((index, _))
    })
    val tupleRDD1 = listRdd.mapPartitionsWithIndex {
      case (index, items) => {
        items.map((index, _))
      }
    }

    tupleRDD.collect().foreach(println)
    tupleRDD1.collect().foreach(println)
```

![image-20200527074827689](/Users/wangfulin/github/image/spark/image-20200527074827689.png)

分区和任务有关

##### flatMap(func) 案例

​    是每一个输入元素可以被映射为0或多个输出元素（func应该返回一个序列，而不是单一元素）,结合映射[mapping]和扁平化[flattening]

```scala
    // flatMap
    val listRdd2 : RDD[List[Int]] = sc.makeRDD(Array(List(1, 2), List(3, 4)))

    val flatMapRDD = listRdd2.flatMap(x => x.map(_ * 2))

    flatMapRDD.collect().foreach(println)
```

可以把它看做是“先映射后扁平化”的快捷操作

> `flatMap`是一种常用的组合子，结合映射[mapping]和扁平化[flattening]。 flatMap需要一个处理嵌套列表的函数，然后将结果串连起来。
>
> ```
> scala> val nestedNumbers = List(List(1, 2), List(3, 4))
> nestedNumbers: List[List[Int]] = List(List(1, 2), List(3, 4))
> 
> scala> nestedNumbers.flatMap(x => x.map(_ * 2))
> res0: List[Int] = List(2, 4, 6, 8)
> ```
>
> 可以把它看做是“先映射后扁平化”的快捷操作：
>
> ```
> scala> nestedNumbers.map((x: List[Int]) => x.map(_ * 2)).flatten
> res1: List[Int] = List(2, 4, 6, 8)
> ```
>
> 这个例子先调用map，然后调用flatten，这就是“组合子”的特征，也是这些函数的本质。

来源：[https://twitter.github.io/scala_school/zh_cn/collections.html#flatMap](https://twitter.github.io/scala_school/zh_cn/collections.html#flatMap)



##### glom案例

将每一个分区形成一个数组，形成新的RDD类型时RDD[Array[T]]

```scala
// glom 算子 将每一个分区形成一个数组，形成新的RDD类型时RDD[Array[T]] 4个分区
    val listRdd2 = sc.makeRDD(1 to 10,4)
    val glamRDD = listRdd2.glom()
    glamRDD.collect().foreach(array => println(array.mkString(",")))
```



##### groupBy(func)案例

​	分组，按照传入函数的返回值进行分组。将相同的key对应的值放入一个迭代器。

```scala
    // groupBy 算子
    // 分组后的数据，形成对偶元组，(k-v)
    val listRdd3 = sc.makeRDD(List(1, 2))
    val groupByRDD = listRdd3.groupBy(x => x % 2)
    groupByRDD.collect.foreach(println)
```

##### filter(func) 案例

​		过滤。

```scala
    // filter算子，数据过滤
    val filterRDD = listRdd3.filter(x => x % 2 == 0)
    filterRDD.collect().foreach(println)
```



##### sample(withReplacement, fraction, seed) 案例

​		以指定的随机种子随机抽样出数量为fraction的数据，withReplacement表示是抽出的数据是否放回，true为有放回的抽样，false为无放回的抽样，seed用于指定随机数生成器种子。

```scala
    // sample(withReplacement, fraction, seed)
    val sampleRDD = listRdd.sample(false, 0.4, 1)
    sampleRDD.collect().foreach(println)
```

##### distinct([numTasks])) 案例

​		对源RDD进行去重后返回一个新的RDD。默认情况下，只有8个并行任务来操作，但是可以传入一个可选的numTasks参数改变它。

![image-20200527101721687](/Users/wangfulin/github/image/spark/image-20200527101721687.png)

```scala
    // distinct
    val distinctRDD = listRdd.distinct()
    distinctRDD.collect().foreach(println)
```

##### coalesce(numPartitions) 案例

作用：缩减分区数，用于大数据集过滤后，提高小数据集的执行效率。

```scala
// coalesce算子 缩减分区 原先是4个分区 改为3个 
val coalesceRDD = listRdd2.coalesce(3)

println(coalesceRDD.partitions.size)
```

##### repartition(numPartitions) 案例

根据分区数，重新通过网络随机洗牌所有数据。

```scala
    // repartition
    val repartitionRDD = listRdd2.repartition(3)
    repartitionRDD.collect().foreach(println)
```

##### coalesce和repartition的区别

1. coalesce重新分区，可以选择是否进行shuffle过程。由参数shuffle: Boolean = false/true决定。

2. repartition实际上是调用的coalesce，默认是进行shuffle的。源码如下：

```scala
def repartition(numPartitions: Int)(implicit ord: Ordering[T] = null): RDD[T] = withScope {
 coalesce(numPartitions, shuffle = true)
}
```

##### sortBy(func,[ascending], [numTasks]) 案例

```scala
// sortBy
val sortByRdd = listRdd.sortBy(x => x)
sortByRdd.collect().foreach(println)
```

改变分区等效于改变任务，用true ， false 升降序

##### union(otherDataset) 案例

```scala
    //  union(otherDataset)
    val listRdd4 = sc.makeRDD(10 to 15)
    listRdd.union(listRdd4).collect().foreach(println)
```

##### subtract (otherDataset) 案例

作用：计算差的一种函数，去除两个RDD中相同的元素，不同的RDD将保留下来

```scala
    // subtract
    listRdd.subtract(listRdd4).collect().foreach(println)

```

##### intersection(otherDataset) 案例

交集后返回一个新的RDD

```scala
    // intersection
    listRdd.intersection(listRdd4).collect().foreach(println)

```

##### zip(otherDataset)案例

​		将两个RDD组合成Key/Value形式的RDD,这里默认两个RDD的partition数量以及元素数量都相同，否则会抛出异常。

要求：分区数相等，每个分区中的数据量相等，不能含有相同的数字

```scala
    // zip
    listRdd.zip(listRdd4).collect().foreach(println)

```



#### Key-Value类型

##### partitionBy案例

作用：对pairRDD进行分区操作，如果原有的partionRDD和现有的partionRDD是一致的话就不进行分区， 否则会生成ShuffleRDD，即会产生shuffle过程。

```scala
    val kvRDD = sc.parallelize(Array((1, "aaa"), (2, "bbb"), (3, "ccc"), (4, "ddd")), 4)

    // 对RDD重新分区
    var kvRDD2 = kvRDD.partitionBy(new org.apache.spark.HashPartitioner(2))

    println(kvRDD2.partitions.size)
```

##### groupByKey案例

groupByKey也是对每个key进行操作，但只生成一个sequence。

```scala
// groupByKey算子
val words = Array("one", "two", "two", "three", "three", "three")
val wordPairsRDD = sc.parallelize(words).map(word => (word, 1))
wordPairsRDD.groupByKey().map(t=>(t._1,t._2.sum))
  .collect().foreach(println)
```

##### reduceByKey(func, [numTasks]) 案例

在一个(K,V)的RDD上调用，返回一个(K,V)的RDD，使用指定的reduce函数，将相同key的值聚合到一起，reduce任务的个数可以通过第二个可选的参数来设置。

```scala
// reduceByKey算子
wordPairsRDD.reduceByKey((x,y)=> x+y).collect().foreach(println)
```

##### reduceByKey和groupByKey的区别

1. reduceByKey：按照key进行聚合，在shuffle之前有combine（预聚合）操作，返回结果是RDD[k,v].（推荐）

2. groupByKey：按照key进行分组，直接进行shuffle。每个数据都需要写入文件，之后再读取文件。

3. 开发指导：reduceByKey比groupByKey，建议使用。但是需要注意是否会影响业务逻辑。

![image-20200527145127734](/Users/wangfulin/github/image/spark/image-20200527145127734.png)

##### aggregateByKey案例

参数：(zeroValue:U,[partitioner: Partitioner]) (seqOp: (U, V) => U,combOp: (U, U) => U)

1. 作用：在kv对的RDD中，按key将value进行分组合并，合并时，将每个value和初始值作为seq函数的参数，进行计算，返回的结果作为一个新的kv对，然后再将结果按照key进行合并，最后将每个分组的value传递给combine函数进行计算（先将前两个value进行计算，将返回结果和下一个value传给combine函数，以此类推），将key与计算结果作为一个新的kv对输出。

2. 参数描述：

（1）zeroValue：给每一个分区中的每一个key一个**初始值**；

（2）seqOp：函数用于在每一个分区中用初始值逐步迭代value；分区内运算规则。

（3）combOp：函数用于合并每个分区中的结果。分区间运算规则。

需求：创建一个pairRDD，取出每个分区相同key对应值的最大值，然后相加

![image-20200527154213260](/Users/wangfulin/github/image/spark/image-20200527154213260.png)



##### foldByKey案例

参数：(zeroValue: V)(func: (V, V) => V): RDD[(K, V)]

分区内和分区间 采用相同的运算规则。

```scala
// foldByKey算子
aggregateByKeyRdd.foldByKey(0)(_+_).collect().foreach(println)
```

##### combineByKey[C] 案例

参数：(createCombiner: V => C, mergeValue: (C, V) => C, mergeCombiners: (C, C) => C) 

1. 作用：对相同K，把V合并成一个集合。

2. 参数描述：

（1）createCombiner: combineByKey() 会遍历分区中的所有元素，因此每个元素的键要么还没有遇到过，要么就和之前的某个元素的键相同。如果这是一个新的元素,**combineByKey()会使用一个叫作createCombiner()的函数来创建那个键对应的累加器的初始值**

（2）mergeValue: 如果这是一个在处理当前分区之前已经遇到的键，它会使用mergeValue()方法将该键的累加器对应的当前值与这个新的值进行合并

（3）mergeCombiners: 由于每个分区都是独立处理的， 因此对于同一个键可以有多个累加器。如果有两个或者更多的分区都有对应同一个键的累加器， 就需要使用用户提供的 mergeCombiners() 方法将各个分区的结果进行合并。

![image-20200530201616173](/Users/wangfulin/github/image/spark/image-20200530201616173.png)

##### sortByKey([ascending], [numTasks]) 案例

作用：在一个(K,V)的RDD上调用，K必须实现Ordered接口，返回一个按照key进行排序的(K,V)的RDD

按照key的正序

```scala
input.sortByKey(true).collect().foreach(println)
```

##### mapValues案例

针对于(K,V)形式的类型只对V进行操作

```scala 
input.mapValues(_+"|||").collect().foreach(println)
```

##### join(otherDataset, [numTasks]) 案例

作用：在类型为(K,V)和(K,W)的RDD上调用，返回一个相同key对应的所有元素对在一起的(K,(V,W))的RDD

##### cogroup(otherDataset, [numTasks]) 案例

作用：在类型为(K,V)和(K,W)的RDD上调用，返回一个(K,(Iterable<V>,Iterable<W>))类型的RDD,将key相同的数据聚合到一个迭代器。

##### 案例实操

```scala
input.cogroup(input2).collect().foreach(println)
```

1. 数据结构：时间戳，省份，城市，用户，广告，中间字段使用空格分割。

```
1516609143867 6 7 64 16

1516609143869 9 4 75 18

1516609143869 1 7 87 12
```

2. 需求：统计出每一个省份广告被点击次数的TOP3

![image-20200530221905063](/Users/wangfulin/github/image/spark/image-20200530221905063.png)

#### Action

#### reduce(func)案例

作用：通过func函数聚集RDD中的**所有元素**，先聚合分区内数据，再聚合分区间数据。

```scala
// reduce
println(actionRdd.reduce(_ + _))
```

##### collect()案例

作用：在驱动程序中，以数组的形式返回数据集的所有元素。

```scala
actionRdd.collect().foreach(println)
```

##### count()案例

返回RDD中元素的个数

##### first()案例

返回RDD中的第一个元素

##### take(n)案例

返回一个由RDD的前n个元素组成的数组

##### takeOrdered(n)案例

返回该RDD排序后的前n个元素组成的**数组**

##### aggregate案例

1. 参数：(zeroValue: U)(seqOp: (U, T) ⇒ U, combOp: (U, U) ⇒ U)

2. 作用：aggregate函数将每个分区里面的元素通过seqOp和初始值进行聚合，然后用combine函数将每个分区的结果和初始值(zeroValue)进行combine操作。这个函数最终返回的类型不需要和RDD中元素类型一致。

分区内会加上初始值，分区间也会加上初始值

##### fold(num)(func)案例

1. 作用：折叠操作，aggregate的简化操作，seqop和combop一样

##### saveAsTextFile(path)

作用：将数据集的元素以textfile的形式保存到HDFS文件系统或者其他支持的文件系统，对于每个元素，Spark将会调用toString方法，将它装换为文件中的文本

##### saveAsSequenceFile(path) 

作用：将数据集中的元素以Hadoop sequencefile的格式保存到指定的目录下，可以使HDFS或者其他Hadoop支持的文件系统。

##### countByKey()案例

作用：针对(K,V)类型的RDD，返回一个(K,Int)的map，表示每一个key对应的元素个数。

##### foreach(func)案例

作用：在数据集的每一个元素上，运行函数func进行更新。



#### RDD中的函数传递

在实际开发中我们往往需要自己定义一些对于RDD的操作，那么此时需要主要的是，**初始化工作是在Driver端进行的，而实际运行程序是在Executor端进行的，**这就涉及到了跨进程通信，是需要序列化的。

使类继承scala.Serializable即可。

#### RDD依赖关系

RDD只支持粗粒度转换，即在大量记录上执行的单个操作。**将创建RDD的一系列Lineage（血统）记录下来，以便恢复丢失的分区。**RDD的Lineage会记录RDD的元数据信息和转换行为，当该RDD的部分分区数据丢失时，它可以根据这些信息来重新运算和恢复丢失的数据分区。

RDD和它依赖的父RDD（s）的关系有两种不同的类型，即窄依赖（narrow dependency）和宽依赖（wide dependency）。

##### 窄依赖

窄依赖指的是每一个父RDD的Partition最多被子RDD的一个Partition使用,窄依赖我们形象的比喻为独生子女

![image-20200531154250237](/Users/wangfulin/github/image/spark/image-20200531154250237.png)

#### 宽依赖

宽依赖指的是多个子RDD的Partition会依赖同一个父RDD的Partition，会引起**shuffle**,总结：宽依赖我们形象的比喻为超生

![image-20200531160115202](/Users/wangfulin/github/image/spark/image-20200531160115202.png)

#### DAG

DAG(Directed Acyclic Graph)叫做有向无环图，原始的RDD通过一系列的转换就就形成了DAG，**根据RDD之间的依赖关系的不同将DAG划分成不同的Stage（阶段）**，对于窄依赖，partition的转换处理在Stage中完成计算。对于宽依赖，由于有Shuffle的存在，只能在parent RDD处理完成后，才能开始接下来的计算，**因此宽依赖是划分Stage的依据**。

![image-20200531161256583](/Users/wangfulin/github/image/spark/image-20200531161256583.png)

#### 任务划分

RDD任务切分中间分为：Application、Job、Stage和Task

1）Application：初始化一个SparkContext即生成一个Application

2）Job：一个Action算子就会生成一个Job

3）Stage：根据RDD之间的依赖关系的不同将Job划分成不同的Stage，遇到一个宽依赖则划分一个Stage。

![image-20200531164015217](/Users/wangfulin/github/image/spark/image-20200531164015217.png)

反向推，**到底有多少阶段 = 1 + shuffle个数，1是这个整体**

4）Task：Stage是一个TaskSet，将Stage划分的结果发送到不同的Executor执行即为一个Task。

注意：Application->Job->Stage-> Task每一层都是1对n的关系。

#### RDD缓存

RDD通过persist方法或cache方法可以将前面的计算结果缓存，默认情况下 persist() 会把数据以序列化的形式缓存在 JVM 的堆空间中。 

但是并不是这两个方法被调用时立即缓存，而是**触发后面的action时，该RDD将会被缓存在计算节点的内存中，并供后面重用。**

![img](/Users/wangfulin/github/image/spark/wpsTVJ8he.png) 

通过查看源码发现cache最终也是调用了persist方法，默认的存储级别都是仅在内存存储一份，Spark的存储级别还有好多种，存储级别在object StorageLevel中定义的。

![img](/Users/wangfulin/github/image/spark/wpsic70jn.png) 

在存储级别的末尾加上“_2”来把持久化数据存为两份![img](/Users/wangfulin/github/image/spark/wpslDlJmG.png)

缓存有可能丢失，或者存储存储于内存的数据由于内存不足而被删除，RDD的缓存容错机制保证了即使缓存丢失也能保证计算的正确执行。通过基于RDD的一系列转换，丢失的数据会被重算，由于RDD的各个Partition是相对独立的，因此只需要计算丢失的部分即可，并不需要重算全部Partition。

#### RDD CheckPoint

Spark中对于数据的保存除了持久化操作之外，还提供了一种检查点的机制，检查点（本质是通过将RDD写入Disk做检查点）是为了通过lineage做容错的辅助，lineage过长会造成容错成本过高，这样就不如在中间阶段做检查点容错，如果之后有节点出现问题而丢失分区，从做检查点的RDD开始重做Lineage，就会减少开销。检查点通过将数据写入到HDFS文件系统实现了RDD的检查点功能。

为当前RDD设置检查点。该函数将会创建一个二进制的文件，并存储到checkpoint目录中，该目录是用[Spark](https://www.iteblog.com/archives/tag/spark/)Context.setCheckpointDir()设置的。在checkpoint的过程中，该RDD的所有依赖于父RDD中的信息将全部被移除。对RDD进行checkpoint操作并不会马上被执行，必须执行Action操作才能触发。



```scala
 sc.setCheckpointDir("hdfs://hadoop102:9000/checkpoint")

```



#### RDD数据分区器

Spark目前**支持Hash分区和Range分区**，用户也可以自定义分区，Hash分区为当前的默认分区，**Spark中分区器直接决定了RDD中分区的个数、RDD中每条数据经过Shuffle过程属于哪个分区和Reduce的个数**

**注意：**

(1) 只有Key-Value类型的RDD才有分区器的，非Key-Value类型的RDD分区器的值是None
(2) 每个RDD的分区ID范围：0~numPartitions-1，决定这个值是属于那个分区的。

##### 获取RDD分区

```scala
val pairs = sc.parallelize(List((1,1),(2,2),(3,3)))
// 查看分区
pairs.partitioner
// 导入HashPartitioner类
import org.apache.spark.HashPartitioner
// 使用HashPartitioner对RDD进行重新分区
val partitioned = pairs.partitionBy(new HashPartitioner(2))
partitioned.partitioner
```

##### Hash分区

HashPartitioner分区的原理：对于给定的key，计算其hashCode，并除以分区的个数取余，如果余数小于0，则用余数+分区的个数（否则加0），最后返回的值就是这个key所属的分区ID。

##### Ranger分区

HashPartitioner分区弊端：**可能导致每个分区中数据量的不均匀，极端情况下会导致某些分区拥有RDD的全部数据。**

RangePartitioner作用：将一定范围内的数映射到某一个分区内，**尽量保证每个分区中数据量的均匀，而且分区与分区之间是有序的**，**一个分区中的元素肯定都是比另一个分区内的元素小或者大，但是分区内的元素是不能保证顺序的。****简单的说就是将一定范围内的数映射到某一个分区内。**实现过程为：

第一步：先从整个RDD中抽取出样本数据，将样本数据排序，计算出每个分区的最大key值，形成一个Array[KEY]类型的数组变量rangeBounds；

第二步：判断key在rangeBounds中所处的范围，给出该key值在下一个RDD中的分区id下标；该分区器要求RDD中的KEY类型必须是可以排序的

##### 自定义分区

要实现自定义的分区器，你需要继承 org.apache.spark.Partitioner 类并实现下面三个方法。 

（1）numPartitions: Int:返回创建出来的分区数。

（2）getPartition(key: Any): Int:返回给定键的分区编号(0到numPartitions-1)。 

（3）equals():Java 判断相等性的标准方法。这个方法的实现非常重要，Spark 需要用这个方法来检查你的分区器对象是否和其他分区器实例相同，这样 Spark 才可以判断两个 RDD 的分区方式是否相同。

需求：将相同后缀的数据写入相同的文件，通过将相同后缀的数据分区到相同的分区并保存输出来实现。

### 数据读取与保存



#### HBase数据库

```scala
    //构建HBase配置信息
    val conf: Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104")
    conf.set(TableInputFormat.INPUT_TABLE, "rddtable")

    //从HBase读取数据形成RDD
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      conf, //配置
      classOf[TableInputFormat], //format的类型
      classOf[ImmutableBytesWritable], // key类型 rowkey
      classOf[Result]) // value类型 Result 查询结果
```



Spark三大数据结构

RDD：分布式数据集

广播变量：分布式只读共享变量

累加器：分布式只写共享变量

### 累加器

累加器用来对信息进行聚合，通常在向 Spark传递函数时，比如使用 map() 函数或者用 filter() 传条件时，可以使用驱动器程序中定义的变量，但是集群中运行的每个任务都会得到这些变量的一份新的副本，更新这些副本的值也不会影响驱动器中的对应变量。如果我们想实现所有分片处理时更新共享变量的功能，那么累加器可以实现我们想要的效果。

累加器使用之前

![image-20200602091812527](/Users/wangfulin/github/image/spark/image-20200602091812527.png)

使用累加器之后：

<img src="/Users/wangfulin/github/image/spark/image-20200602091656089.png" alt="image-20200602091656089" style="zoom:67%;" />

##### 自定义累加器

实现自定义类型累加器需要继承AccumulatorV2并至少覆写下例中出现的方法。

参数是in, out

1.继承AccumulatorV2

2.实现抽象方法

3.创建累加器

```scala
class LogAccumulator extends org.apache.spark.util.AccumulatorV2[String, java.util.Set[String]] {
  private val _logArray: java.util.Set[String] = new java.util.HashSet[String]()

  // 初始化
  override def isZero: Boolean = {
    _logArray.isEmpty
  }

  override def reset(): Unit = {
    _logArray.clear()
  }

  // 添加
  override def add(v: String): Unit = {
    _logArray.add(v)
  }

  // 合并
  override def merge(other: org.apache.spark.util.AccumulatorV2[String, java.util.Set[String]]): Unit = {
    other match {
      case o: LogAccumulator => _logArray.addAll(o.value)
    }

  }

  override def value: java.util.Set[String] = {
    java.util.Collections.unmodifiableSet(_logArray)
  }

  override def copy():org.apache.spark.util.AccumulatorV2[String, java.util.Set[String]] = {
    val newAcc = new LogAccumulator()
    _logArray.synchronized{
      newAcc._logArray.addAll(_logArray)
    }
    newAcc
  }
}
```

调用：

创建累加器：

```scala
val logAccumulator = new LogAccumulator
```

注册累加器:

```scala
sc.register(logAccumulator)
```

### 广播变量

广播变量用来高效分发较大的对象。向所有工作节点发送一个较大的只读值，以供一个或多个Spark操作使用。 在多个并行操作中使用同一个变量，但是 Spark会为每个任务分别发送。

变量只会被发到各个节点一次，应作为只读值处理(修改这个值不会影响到别的节点)。



### SparkCore总结

![Spark Core 总结](/Users/wangfulin/github/image/spark/Spark Core 总结.png)

