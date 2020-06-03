package com.wangfulin

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkRDD {

  def main(args: Array[String]): Unit = {
    val config = new SparkConf().setMaster("local[2]").setAppName("SparkRDD")

    // 创建上下文对象
    val sc = new SparkContext(config)

    // map算子
    val listRdd = sc.makeRDD(1 to 10)

    val mapRdd = listRdd.map(_ * 2)

    mapRdd.collect().foreach(println)

    // 可以对一个RDD中的所有分区进行遍历
    // 效率高于map算子，减少发送到执行器的交互次数
    // 但是可能因为内存问题，造成OOM
    // 假设有N个元素，有M个分区，那么map的函数的将被调用N次,
    // 而mapPartitions被调用M次,一个函数一次处理所有分区。
    val mapPartitionsRdd = listRdd.mapPartitions(datas => {
      datas.map(_ * 2)
    })

    mapPartitionsRdd.collect().foreach(println)

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

    // flatMap 算子
    val listRdd1 = sc.makeRDD(List(List(1, 2), List(3, 4)))

    val flatMapRDD = listRdd1.flatMap(x => x.map(_ * 2))


    flatMapRDD.collect().foreach(println)


    // glom 算子 将每一个分区形成一个数组，形成新的RDD类型时RDD[Array[T]] 4个分区
    val listRdd2 = sc.makeRDD(1 to 10, 4)
    val glamRDD = listRdd2.glom()
    glamRDD.collect().foreach(array => println(array.mkString(",")))


    // groupBy 算子
    // 分组后的数据，形成对偶元组，(k-v)
    val listRdd3 = sc.makeRDD(List(1, 2))
    val groupByRDD = listRdd3.groupBy(x => x % 2)
    groupByRDD.collect.foreach(println)

    // filter算子，数据过滤
    val filterRDD = listRdd3.filter(x => x % 2 == 0)
    filterRDD.collect().foreach(println)

    // sample(withReplacement, fraction, seed)
    val sampleRDD = listRdd.sample(false, 0.4, 1)
    sampleRDD.collect().foreach(println)

    // distinct
    val distinctRDD = listRdd.distinct()
    distinctRDD.collect().foreach(println)

    // coalesce算子 缩减分区 原先是4个分区 改为3个
    val coalesceRDD = listRdd2.coalesce(3)

    println(coalesceRDD.partitions.size)


    // repartition
    val repartitionRDD = listRdd2.repartition(3)
    repartitionRDD.collect().foreach(println)


    // sortBy
    val sortByRdd = listRdd.sortBy(x => x)
    sortByRdd.collect().foreach(println)

    //  union(otherDataset)
    val listRdd4 = sc.makeRDD(11 to 20)
    listRdd.union(listRdd4).collect().foreach(println)

    // subtract
    listRdd.subtract(listRdd4).collect().foreach(println)

    // intersection
    listRdd.intersection(listRdd4).collect().foreach(println)

    // zip
    listRdd.zip(listRdd4).collect().foreach(println)


    /** ************(key - value) ***********/

    val kvRDD = sc.parallelize(Array((1, "aaa"), (2, "bbb"), (3, "ccc"), (4, "ddd")), 4)

    // partitionBy算子 对RDD重新分区
    var kvRDD2 = kvRDD.partitionBy(new org.apache.spark.HashPartitioner(2))

    println(kvRDD2.partitions.size)


    // groupByKey算子
    val words = Array("one", "two", "two", "three", "three", "three")
    val wordPairsRDD = sc.parallelize(words).map(word => (word, 1))
    wordPairsRDD.groupByKey().map(t => (t._1, t._2.sum))
      .collect().foreach(println)

    // reduceByKey算子
    wordPairsRDD.reduceByKey((x, y) => x + y).collect().foreach(println)

    // aggregateByKey算子 出每个分区相同key对应值的最大值，然后相加
    val aggregateByKeyRdd = sc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
    aggregateByKeyRdd.aggregateByKey(0)(Math.max(_, _), _ + _).collect().foreach(println)

    // foldByKey算子
    aggregateByKeyRdd.foldByKey(0)(_ + _).collect().foreach(println)

    // combineByKey算子 区间内统计 区间内求和 区间间求除法
    val input = sc.parallelize(Array(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98)), 2)
    val combine = input.combineByKey((_, 1), (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1), (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2))
    combine.collect

    val result = combine.map { case (key, value) => (key, value._1 / value._2.toDouble) }
    //    val result = combine.map(data =>{
    //      (data._1,data._1 / data._2)
    //    })

    // sortByKey
    input.sortByKey(true).collect().foreach(println)

    // mapValues
    input.mapValues(_ + "|||").collect().foreach(println)

    val input2 = sc.parallelize(Array(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98)), 2)

    //join
    input.join(input2).collect().foreach(println)

    // cogroup
    input.cogroup(input2).collect().foreach(println)


    // 统计出每一个省份广告被点击次数的TOP3
    val line = sc.textFile("/Users/wangfulin/bigdata/data/agent.log")
    val provinceAdToOne = line.map(x => {
      val datas = x.split(" ")
      ((datas(1), datas(4)), 1)

    })
    // 每个省中  相同的key被点击的总数
    val provinceAdSum = provinceAdToOne.reduceByKey(_ + _)

    provinceAdSum.take(5).foreach(println)

    // ((省，广告),总数) 返回 (省，（广告,总数）)
    val provinceToAdSum = provinceAdSum.map(x => {
      (x._1._1, (x._1._2, x._2))
    })

    provinceToAdSum.take(5).foreach(println)

    // 按省分类
    val provinceGroup = provinceToAdSum.groupByKey()

    provinceGroup.take(5).foreach(println)

    val provinceAdTop3 = provinceGroup.mapValues { x =>

      x.toList.sortWith((x, y) => x._2 > y._2).take(3) //list 里面两两相互比较
    }.foreach(println)


    /** *****Action *******/
    val actionRdd = sc.makeRDD(1 to 10, 2)

    // reduce
    println(actionRdd.reduce(_ + _))

    // collect
    actionRdd.collect().foreach(println)

    //count
    println(actionRdd.count())

  }
}
