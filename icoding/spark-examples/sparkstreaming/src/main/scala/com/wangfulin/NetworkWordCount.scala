package com.wangfulin

/**
 * Spark Streaming处理Socket数据
 *
 * 测试： nc
 */

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object NetworkWordCount {

  def main(args: Array[String]): Unit = {


    val sparkConf = new SparkConf().setMaster("local[4]").setAppName("NetworkWordCount")

    /**
     * 创建StreamingContext需要两个参数：SparkConf和batch interval
     */
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.socketTextStream("localhost", 6339)

    val result = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print()

    // 启动采集器
    ssc.start()
    // 等待采集器完成
    ssc.awaitTermination()
  }
}
