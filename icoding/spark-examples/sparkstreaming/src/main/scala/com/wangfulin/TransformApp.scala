package com.wangfulin

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 黑名单过滤 Transform操作
 */

object TransformApp {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("TransformApp")

    /**
     * 创建StreamingContext需要两个参数：SparkConf和batch interval
     */
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    /**
     * 构建黑名单
     */
    val blacks = List("zs", "ls")
    // ==>(zs: true)(ls: true)
    val blacksRDD = ssc.sparkContext.parallelize(blacks).map(x => (x, true))

    val lines = ssc.socketTextStream("localhost", 6789)

    // 输入 20180808,zs
    val clickLog = lines.map(x => (x.split(",")(1), x)) //(zs: 20180808,zs) DStream ==> RDD
      .transform(rdd => {
        rdd.leftOuterJoin(blacksRDD) // (zs: [<20180808,zs>, <true>])
          .filter(x => x._2._2.getOrElse(false) != true) //第二位里面的第二位 不等于true的留下
          .map(x => x._2._1) // 只需要第二个里面的第一个数据
      })
    clickLog.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
