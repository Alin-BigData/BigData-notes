# Spark-SQL

### Hadoop环境搭建

1) 下载Hadoop
	http://archive.cloudera.com/cdh5/cdh/5/
	2.6.0-cdh5.7.0

	wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.7.0.tar.gz

2）安装jdk
	下载
	解压到app目录：tar -zxvf jdk-7u51-linux-x64.tar.gz -C ~/app/
	验证安装是否成功：~/app/jdk1.7.0_51/bin      ./java -version
	建议把bin目录配置到系统环境变量(~/.bash_profile)中
		export JAVA_HOME=/home/hadoop/app/jdk1.7.0_51
		export PATH=$JAVA_HOME/bin:$PATH

3）机器参数设置
	hostname: hadoop001

	修改机器名: /etc/sysconfig/network
		NETWORKING=yes
		HOSTNAME=hadoop001
	
	设置ip和hostname的映射关系: /etc/hosts
		192.168.199.200 hadoop001
		127.0.0.1 localhost
	
	ssh免密码登陆(本步骤可以省略，但是后面你重启hadoop进程时是需要手工输入密码才行)
		ssh-keygen -t rsa
		cp ~/.ssh/id_rsa.pub ~/.ssh/authorized_keys

4）Hadoop配置文件修改: ~/app/hadoop-2.6.0-cdh5.7.0/etc/hadoop
	hadoop-env.sh
		

```
export  JAVA_HOME=/home/hadoop/app/jdk1.7.0_51
```

修改core-site.xml

	<property>
	    	<name>fs.defaultFS</name>
	    	<value>hdfs://hadoop001:8020</value>
		</property>	
	
		<property>
	    	<name>hadoop.tmp.dir</name>
	    	<value>/home/hadoop/app/tmp</value>
		</property>	


​	

hdfs-site.xml

```
<property>
       <name>dfs.replication</name>
       <value>1</value>
 </property>
```

5）格式化HDFS
	注意：这一步操作，只是在第一次时执行，每次如果都格式化的话，那么HDFS上的数据就会被清空
	bin/hdfs namenode -format

6）启动HDFS
	sbin/start-dfs.sh

	验证是否启动成功:
		jps
			DataNode
			SecondaryNameNode
			NameNode
	
		浏览器
			http://hadoop001:50070/

7）停止HDFS
	sbin/stop-dfs.sh

### YARN架构

1 RM(ResourceManager) + N NM(NodeManager)

ResourceManager的职责： 一个集群active状态的RM只有一个，负责整个集群的资源管理和调度
1）处理客户端的请求(启动/杀死)
2）启动/监控ApplicationMaster(一个作业对应一个AM)
3）监控NM
4）系统的资源分配和调度


NodeManager：整个集群中有N个，负责单个节点的资源管理和使用以及task的运行情况
1）定期向RM汇报本节点的资源使用请求和各个Container的运行状态
2）接收并处理RM的container启停的各种命令
3）单个节点的资源管理和任务管理

ApplicationMaster：每个应用/作业对应一个，负责应用程序的管理
1）数据切分
2）为应用程序向RM申请资源(container)，并分配给内部任务
3）与NM通信以启停task， task是运行在container中的
4）task的监控和容错

Container：
对任务运行情况的描述：cpu、memory、环境变量

#### YARN执行流程

1）用户向YARN提交作业
2）RM为该作业分配第一个container(AM)
3）RM会与对应的NM通信，要求NM在这个container上启动应用程序的AM
4) AM首先向RM注册，然后AM将为各个任务申请资源，并监控运行情况
5）AM采用轮训的方式通过RPC协议向RM申请和领取资源
6）AM申请到资源以后，便和相应的NM通信，要求NM启动任务
7）NM启动我们作业对应的task



#### YARN环境搭建

mapred-site.xml

```xml
	<property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
   </property>


```

yarn-site.xml

```xml
	<property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
```

启动yarn：sbin/start-yarn.sh

验证是否启动成功
	jps
		ResourceManager
		NodeManager

	web: http://localhost:8088

停止yarn： sbin/stop-yarn.sh

提交mr作业到yarn上运行： wc(wordcount例子)

/home/hadoop/app/hadoop-2.6.0-cdh5.7.0/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0-cdh5.7.0.jar

```
hadoop jar /home/hadoop/app/hadoop-2.6.0-cdh5.7.0/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0-cdh5.7.0.jar wordcount /input/wc/hello.txt /output/wc/
```

当我们再次执行该作业时，会报错：
FileAlreadyExistsException: 
Output directory hdfs://hadoop001:8020/output/wc already exists



Hive底层的执行引擎有：MapReduce、Tez、Spark
	Hive on MapReduce
	Hive on Tez
	Hive on Spark

压缩：GZIP、LZO、Snappy、BZIP2..
存储：TextFile、SequenceFile、RCFile、ORC、Parquet
UDF：自定义函数



### Hive环境搭建

1）Hive下载：http://archive.cloudera.com/cdh5/cdh/5/
	wget http://archive.cloudera.com/cdh5/cdh/5/hive-1.1.0-cdh5.7.0.tar.gz

2）解压
	tar -zxvf hive-1.1.0-cdh5.7.0.tar.gz -C ~/app/

3）配置
	系统环境变量(~/.bahs_profile)

```
export HIVE_HOME=/home/hadoop/app/hive-1.1.0-cdh5.7.0
export PATH=$HIVE_HOME/bin:$PATH
```

实现安装一个mysql， yum install xxx

hive-site.xml ： sparksql数据库名称

	<property>
	  		<name>javax.jdo.option.ConnectionURL</name>
	    	<value>jdbc:mysql://localhost:3306/sparksql?createDatabaseIfNotExist=true</value>
	    </property>
	    
	 <property>
		<name>javax.jdo.option.ConnectionDriverName</name>
	    <value>com.mysql.jdbc.Driver</value>
	  </property>
	  
	  <property>
			<name>javax.jdo.option.ConnectionUserName</name>
	    	<value>root</value>
	    </property>
	    
	   <property>
	    <name>javax.jdo.option.ConnectionPassword</name>
	    	<value>root</value>
	    </property>
参考：[https://github.com/apache/hive/blob/master/data/conf/hive-site.xml](https://github.com/apache/hive/blob/master/data/conf/hive-site.xml)
    

4）拷贝mysql驱动到$HIVE_HOME/lib/

5）启动hive: $HIVE_HOME/bin/hive

创建表

```
CREATE  TABLE table_name 
  [(col_name data_type [COMMENT col_comment])]
create table hive_wordcount(context string);
```

加载数据到hive表

```
LOAD DATA LOCAL INPATH 'filepath' INTO TABLE tablename 

load data local inpath '/home/hadoop/data/hello.txt' into table hive_wordcount;

select word, count(1) from hive_wordcount lateral view explode(split(context,'\t')) wc as word group by word;
```

lateral view explode(): 是把每行记录按照指定分隔符进行拆解

hive ql提交执行以后会生成mr作业，并在yarn上运行

```
create table emp(
empno int,
ename string,
job string,
mgr int,
hiredate string,
sal double,
comm double,
deptno int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

create table dept(
deptno int,
dname string,
location string
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

load data local inpath '/home/hadoop/data/dept.txt' into table dept;

求每个部门的人数
select deptno, count(1) from emp group by deptno;
```

### MapReduce 和 Spark

MapReduce的局限性：
1）代码繁琐；
2）只能够支持map和reduce方法；
3）执行效率低下；
4）不适合迭代多次、交互式、流式的处理；

框架多样化：
1）批处理（离线）：MapReduce、Hive、Pig
2）流式处理（实时）： Storm、JStorm
3）交互式计算：Impala

学习、运维成本无形中都提高了很多

===> Spark 



### 提交Spark Application到环境中运行

```shell
spark-submit \
--name SQLContextApp \
--class com.imooc.spark.SQLContextApp \
--master local[2] \
/home/hadoop/lib/sql-1.0.jar \
/home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json
```

注意：
1）To use a HiveContext, you do not need to have an existing Hive setup
2）hive-site.xml



### Spark SQL执行过程

```sql
create table t(key string, value string);
explain extended select a.key*(2+3), b.value from  t a join t b on a.key = b.key and a.key > 3;

== Parsed Logical Plan ==
'Project [unresolvedalias(('a.key * (2 + 3)), None), 'b.value]
+- 'Join Inner, (('a.key = 'b.key) && ('a.key > 3))
   :- 'UnresolvedRelation `t`, a
   +- 'UnresolvedRelation `t`, b

== Analyzed Logical Plan ==
(CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE)): double, value: string
Project [(cast(key#321 as double) * cast((2 + 3) as double)) AS (CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE))#325, value#324]
+- Join Inner, ((key#321 = key#323) && (cast(key#321 as double) > cast(3 as double)))
   :- SubqueryAlias a
   :  +- MetastoreRelation default, t
   +- SubqueryAlias b
      +- MetastoreRelation default, t

== Optimized Logical Plan ==
Project [(cast(key#321 as double) * 5.0) AS (CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE))#325, value#324]
+- Join Inner, (key#321 = key#323)
   :- Project [key#321]
   :  +- Filter (isnotnull(key#321) && (cast(key#321 as double) > 3.0))
   :     +- MetastoreRelation default, t
   +- Filter (isnotnull(key#323) && (cast(key#323 as double) > 3.0))
      +- MetastoreRelation default, t

== Physical Plan ==
*Project [(cast(key#321 as double) * 5.0) AS (CAST(key AS DOUBLE) * CAST((2 + 3) AS DOUBLE))#325, value#324]
+- *SortMergeJoin [key#321], [key#323], Inner
   :- *Sort [key#321 ASC NULLS FIRST], false, 0
   :  +- Exchange hashpartitioning(key#321, 200)
   :     +- *Filter (isnotnull(key#321) && (cast(key#321 as double) > 3.0))
   :        +- HiveTableScan [key#321], MetastoreRelation default, t
   +- *Sort [key#323 ASC NULLS FIRST], false, 0
      +- Exchange hashpartitioning(key#323, 200)
         +- *Filter (isnotnull(key#323) && (cast(key#323 as double) > 3.0))
            +- HiveTableScan [key#323, value#324], MetastoreRelation default, t
```



### thriftserver/beeline的使用

1) 启动thriftserver: 默认端口是10000 ，可以修改
2）启动beeline
beeline -u jdbc:hive2://localhost:10000 -n hadoop

修改thriftserver启动占用的默认端口号：

```shell
./start-thriftserver.sh  \
--master local[2] \
--jars ~/software/mysql-connector-java-5.1.27-bin.jar  \
--hiveconf hive.server2.thrift.port=14000 

beeline -u jdbc:hive2://localhost:14000 -n hadoop
```

### thriftserver和普通的spark-shell/spark-sql有什么区别

1）spark-shell、spark-sql都是一个spark  application；
2）thriftserver， 不管你启动多少个客户端(beeline/code)，永远都是一个spark application解决了一个数据共享的问题，多个客户端可以共享数据；

注意事项：在使用jdbc开发时，一定要先启动thriftserver

```shell
Exception in thread "main" java.sql.SQLException: 
Could not open client transport with JDBC Uri: jdbc:hive2://hadoop001:14000: 
java.net.ConnectException: Connection refused
```

### SQLContext&&HiveContext

#### SQLContext

```
object SQLContextApp {
  def main(args: Array[String]): Unit = {
    val path = "/Users/wangfulin/bigdata/data/people.json"

    //1)创建相应的Context
    val sparkConf = new SparkConf()

    //在测试或者生产中，AppName和Master我们是通过脚本进行指定
    sparkConf.setAppName("SQLContextApp").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)

    //2)相关的处理: json
    val people = sqlContext.read.format("json").load(path)
    people.printSchema()
    people.show()
    //3)关闭资源
    sc.stop()

  }

}
```

#### HiveContext

```
object HiveContextApp {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    //在测试或者生产中，AppName和Master我们是通过脚本进行指定
    //sparkConf.setAppName("HiveContextApp").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)
    val hiveContext = new HiveContext(sc)

    //2)相关的处理:
    hiveContext.table("emp").show

    //3)关闭资源
    sc.stop()
  }

}

```



### DataFram&&DataSet

DataFrame它不是Spark SQL提出的，而是早起在R、Pandas语言就已经有了的。


A Dataset is a distributed collection of data：分布式的数据集

A DataFrame is a Dataset organized into named columns. 
以列（列名、列的类型、列值）的形式构成的分布式数据集，按照列赋予不同的名称

student
id:int
name:string
city:string


It is conceptually equivalent to a table in a relational database 
or a data frame in R/Python


RDD： 
	java/scala  ==> jvm
	python ==> python runtime


DataFrame:
	java/scala/python ==> Logic Plan


DataFrame和RDD互操作的两种方式：
1）反射：case class   前提：事先需要知道你的字段、字段类型    
2）编程：Row          如果第一种情况不能满足你的要求（事先不知道列）
3) 选型：优先考虑第一种



val rdd = spark.sparkContext.textFile("file:///home/hadoop/data/student.data")



DataFrame = Dataset[Row]
Dataset：强类型  typed  case class
DataFrame：弱类型   Row


SQL: 
	seletc name from person;  compile  ok, result no

DF:
	df.select("name")  compile no
	df.select("nname")  compile ok  

DS:
	ds.map(line => line.itemid)  compile no

![image-20200519132551718](../image/spark/image-20200519132551718.png)

#### DataFrame

```scala
// DataFrame API基本操作
object DataFrameApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    // 将json文件加载成一个dataframe
    val peopleDF = spark.read.format("json").load("file:///Users/wangfulin/bigdata/data/people.json")

    // 输出dataframe对应的schema信息
    peopleDF.printSchema()

    // 输出数据集的前20条记录
    peopleDF.show()

    //查询某列所有的数据： select name from table
    peopleDF.select("name").show()

    // 查询某几列所有的数据，并对列进行计算： select name, age+10 as age2 from table
    peopleDF.select(peopleDF.col("name"), (peopleDF.col("age") + 10).as("age2")).show()

    //根据某一列的值进行过滤： select * from table where age>19
    peopleDF.filter(peopleDF.col("age") > 19).show()

    //根据某一列进行分组，然后再进行聚合操作： select age,count(1) from table group by age
    peopleDF.groupBy("age").count().show()

    spark.stop()
  }

}
```

#### DataFrameCase

```
object DataFrameCase {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataFrameRDDApp").master("local[2]").getOrCreate()

    // RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/student.data")

    //注意：需要导入隐式转换
    import spark.implicits._
    val studentDF = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()

    //show默认只显示前20条 默认超过一定长度截掉
    studentDF.show
    studentDF.show(30)
    studentDF.show(30, false)

    studentDF.take(10)
    studentDF.first()
    studentDF.head(3)

    studentDF.select("email").show(30, false)


    // 过滤
    studentDF.filter("name=''").show
    studentDF.filter("name='' OR name='NULL'").show
    //name以M开头的人
    studentDF.filter("SUBSTR(name,0,1)='w'").show

    // 排序
    studentDF.sort(studentDF("name")).show
    studentDF.sort(studentDF("name").desc).show

    studentDF.sort("name","id").show
    studentDF.sort(studentDF("name").asc, studentDF("id").desc).show

    // 重命名
    studentDF.select(studentDF("name").as("student_name")).show


    val studentDF2 = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()

    // join 默认 inner join
    studentDF.join(studentDF2, studentDF.col("id") === studentDF2.col("id")).show

    spark.stop()
  }

  case class Student(id: Int, name: String, phone: String, email: String)
}
```

#### DataFrame和RDD的互操作

有两种方式

```scala
object DataFrameRDDApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataFrameRDDApp").master("local[2]").getOrCreate()

    //inferReflection(spark)

    program(spark)

    spark.stop()
  }

  // 第二种方式 编程的方式
  def program(spark: SparkSession): Unit = {
    // RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/info.txt")

    val infoRDD = rdd.map(_.split(",")).map(line => Row(line(0).toInt, line(1), line(2).toInt))

    val structType = StructType(Array(StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true)))

    val infoDF = spark.createDataFrame(infoRDD, structType)
    infoDF.printSchema()
    infoDF.show()


    //通过df的api进行操作
    infoDF.filter(infoDF.col("age") > 1).show

    //通过sql的方式进行操作
    infoDF.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age > 1").show()
  }


  // 第一种方式，采用反射的方式
  def inferReflection(spark: SparkSession) {
    // RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/info.txt")

    //注意：需要导入隐式转换
    import spark.implicits._
    val infoDF = rdd.map(_.split(",")).map(line => Info(line(0).toInt, line(1), line(2).toInt)).toDF()

    infoDF.show()

    infoDF.filter(infoDF.col("age") > 1).show

    infoDF.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age > 1").show()
  }

  case class Info(id: Int, name: String, age: Int)

}
```

#### Dataset

```
object DatasetApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DatasetApp")
      .master("local[2]").getOrCreate()

    //注意：需要导入隐式转换
    import spark.implicits._

    // csv路径
    val path = "file:///Users/wangfulin/bigdata/data/sales.csv"

    //spark如何解析csv文件？ 返回的是dataframe类型
    val df = spark.read.option("header", "true").option("inferSchema", "true").csv(path)
    df.show

    // df 转成 ds ： as一个类型，就完成转换了
    val ds = df.as[Sales]
    ds.map(line => line.itemId).show
  }

  case class Sales(transactionId: Int, customerId: Int, itemId: Int, amountPaid: Double)

}
```

### 引入外部数据源

#### 处理parquet数据

```java
object ParquetApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkSessionApp")
      .master("local[2]").getOrCreate()


    /**
     * spark.read.format("parquet").load 这是标准写法
     */
    val userDF = spark.read.format("parquet").load("file:///Users/wangfulin/bigdata/data/users.parquet")

    userDF.printSchema()
    userDF.show()

    userDF.select("name", "favorite_color").show

    userDF.select("name", "favorite_color").write.format("json").save("file:///Users/wangfulin/bigdata/data/tmp/jsonout")

    spark.read.load("file:///Users/wangfulin/bigdata/data/users.parquet").show

    //会报错，因为sparksql默认处理的format就是parquet
    spark.read.load("file:///Users/wangfulin/bigdata/data/people.json").show

    spark.read.format("parquet").option("path", "file:///Users/wangfulin/bigdata/data/users.parquet").load().show
    spark.stop()
  }
}
```

```shell
org.apache.spark.sql.AnalysisException: Attribute name "count(1)" contains invalid character(s) among " ,;{}()\n\t=". Please use alias to rename it.;

spark.sqlContext.setConf("spark.sql.shuffle.partitions","10")
```

在生产环境中一定要注意设置spark.sql.shuffle.partitions，默认是200



#### 操作MySQL的数据

方法一：

```
spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306/hive").option("dbtable", "hive.TBLS").option("user", "root").option("password", "root").option("driver", "com.mysql.jdbc.Driver").load()
```

需要添加driver：

java.sql.SQLException: No suitable driver

方法二：

```
import java.util.Properties
val connectionProperties = new Properties()
connectionProperties.put("user", "root")
connectionProperties.put("password", "root")
connectionProperties.put("driver", "com.mysql.jdbc.Driver")

val jdbcDF2 = spark.read.jdbc("jdbc:mysql://localhost:3306", "hive.TBLS", connectionProperties)
```

方法三：

```java
CREATE TEMPORARY VIEW jdbcTable
USING org.apache.spark.sql.jdbc
OPTIONS (
  url "jdbc:mysql://localhost:3306",
  dbtable "hive.TBLS",
  user 'root',
  password 'root',
  driver 'com.mysql.jdbc.Driver'
)
```

外部数据源综合案例

```mysql
create database spark;
use spark;

CREATE TABLE DEPT(
DEPTNO int(2) PRIMARY KEY,
DNAME VARCHAR(14) ,
LOC VARCHAR(13) ) ;

INSERT INTO DEPT VALUES(10,'ACCOUNTING','NEW YORK');
INSERT INTO DEPT VALUES(20,'RESEARCH','DALLAS');
INSERT INTO DEPT VALUES(30,'SALES','CHICAGO');
INSERT INTO DEPT VALUES(40,'OPERATIONS','BOSTON');
```

```scala
/**
 * 使用外部数据源综合查询Hive和MySQL的表数据
 */
object HiveMySQLApp {

  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("HiveMySQLApp")
      .master("local[2]").getOrCreate()

    // 加载Hive表数据
    val hiveDF = spark.table("emp")

    // 加载MySQL表数据
    val mysqlDF = spark.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306").option("dbtable", "spark.DEPT").option("user", "root").option("password", "root").option("driver", "com.mysql.jdbc.Driver").load()

    // JOIN
    val resultDF = hiveDF.join(mysqlDF, hiveDF.col("deptno") === mysqlDF.col("DEPTNO"))
    resultDF.show
    
    resultDF.select(hiveDF.col("empno"), hiveDF.col("ename"),
      mysqlDF.col("deptno"), mysqlDF.col("dname")).show

    spark.stop()
  }
}
```



### 日志分析实战

数据源：[https://pan.baidu.com/s/19KRj1Td_aXff9WZ0Ps694g](https://pan.baidu.com/s/19KRj1Td_aXff9WZ0Ps694g)

[https://github.com/lemonahit/LogAnalysis/issues/1](https://github.com/lemonahit/LogAnalysis/issues/1)

ip地址解析：[https://blog.csdn.net/u010886217/article/details/86764556](https://blog.csdn.net/u010886217/article/details/86764556)

```shell
mvn install:install-file -Dfile="/Users/wangfulin/Desktop/ipdatabase/target/ipdatabase-1.0-SNAPSHOT.jar" '-DgroupId=com.ggstar' '-DartifactId=ipdatabase' '-Dversion=1.0' '-Dpackaging=jar'
```

问题

```java
java.io.FileNotFoundException: file:/Users/wangfulin/.m2/repository/com/ggstar/ipdatabase/1.0/ipdatabase-1.0.jar!/ipRegion.xlsx (No such file or directory)
```

解决方案：需要把ipdatabase 下面ipDatabase.csv,ipRegion.xlsx 两个文件拷贝到resource文件夹下面

![image-20200519194232607](../image/spark/image-20200519194232607.png)



用户行为日志：用户每次访问网站时所有的行为数据（访问、浏览、搜索、点击...）
	用户行为轨迹、流量日志


日志数据内容：
1）访问的系统属性： 操作系统、浏览器等等
2）访问特征：点击的url、从哪个url跳转过来的(referer)、页面上的停留时间等
3）访问信息：session_id、访问ip(访问城市)等

```
2013-05-19 13:00:00     http://www.taobao.com/17/?tracker_u=1624169&type=1      B58W48U4WKZCJ5D1T3Z9ZY88RU7QA7B1        http://hao.360.cn/      1.196.34.243   
```

数据处理流程
1）数据采集
	Flume： web日志写入到HDFS

2）数据清洗
	脏数据
	Spark、Hive、MapReduce 或者是其他的一些分布式计算框架  
	清洗完之后的数据可以存放在HDFS(Hive/Spark SQL)

3）数据处理
	按照我们的需要进行相应业务的统计和分析
	Spark、Hive、MapReduce 或者是其他的一些分布式计算框架

4）处理结果入库
	结果可以存放到RDBMS、NoSQL

5）数据的可视化
	通过图形化展示的方式展现出来：饼图、柱状图、地图、折线图
	ECharts、HUE、Zeppelin


一般的日志处理方式，我们是需要进行分区的，
按照日志中的访问时间进行相应的分区，比如：d,h,m5(每5分钟一个分区)


输入：访问时间、访问URL、耗费的流量、访问IP地址信息
输出：URL、cmsType(video/article)、cmsId(编号)、流量、ip、城市信息、访问时间、天



ip解析，使用github上已有的开源项目
1）git clone https://github.com/wzhe06/ipdatabase.git
2）编译下载的项目：mvn clean package -DskipTests
3）安装jar包到自己的maven仓库

```
mvn install:install-file -Dfile=/Users/rocky/source/ipdatabase/target/ipdatabase-1.0-SNAPSHOT.jar -DgroupId=com.ggstar -DartifactId=ipdatabase -Dversion=1.0 -Dpackaging=jar
```


　

**调优点：**
1) 控制文件输出的大小： coalesce
2) 分区字段的数据类型调整：spark.sql.sources.partitionColumnTypeInference.enabled
3) 批量插入数据库数据，提交使用batch操作

```mysql
create table day_video_access_topn_stat (
day varchar(8) not null,
cms_id bigint(10) not null,
times bigint(10) not null,
primary key (day, cms_id)
);


create table day_video_city_access_topn_stat (
day varchar(8) not null,
cms_id bigint(10) not null,
city varchar(20) not null,
times bigint(10) not null,
times_rank int not null,
primary key (day, cms_id, city)
);

create table day_video_traffics_topn_stat (
day varchar(8) not null,
cms_id bigint(10) not null,
traffics bigint(20) not null,
primary key (day, cms_id)
);
```



### Spark SQL重点掌握

![image-20200520105703831](../image/spark/image-20200520105703831.png)

```scala
即席查询
普通查询

Load Data
1) RDD    DataFrame/Dataset
2) Local   Cloud(HDFS/S3)


将数据加载成RDD
val masterLog = sc.textFile("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/logs/spark-hadoop-org.apache.spark.deploy.master.Master-1-hadoop001.out")
val workerLog = sc.textFile("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/logs/spark-hadoop-org.apache.spark.deploy.worker.Worker-1-hadoop001.out")
val allLog = sc.textFile("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/logs/*out*")

masterLog.count
workerLog.count
allLog.count

存在的问题：使用使用SQL进行查询呢？

import org.apache.spark.sql.Row
val masterRDD = masterLog.map(x => Row(x))
import org.apache.spark.sql.types._
val schemaString = "line"

val fields = schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, nullable = true))
val schema = StructType(fields)

val masterDF = spark.createDataFrame(masterRDD, schema)
masterDF.show


JSON/Parquet
val usersDF = spark.read.format("parquet").load("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet")
usersDF.show


spark.sql("select * from  parquet.`file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet`").show

Drill 大数据处理框架


从Cloud读取数据: HDFS/S3
val hdfsRDD = sc.textFile("hdfs://path/file")
val s3RDD = sc.textFile("s3a://bucket/object")
	s3a/s3n

spark.read.format("text").load("hdfs://path/file")
spark.read.format("text").load("s3a://bucket/object")





val df=spark.read.format("json").load("file:///home/hadoop/app/spark-2.1.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json")

df.show
TPC-DS
spark-packages.org

```





----

代码：

- [sparksql](../icoding/spark-examples/sparksql)

