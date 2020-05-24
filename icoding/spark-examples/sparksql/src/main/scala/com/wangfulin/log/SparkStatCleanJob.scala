package com.wangfulin.log

import com.wangfulin.log.utils.AccessConvertUtil
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 使用Spark完成我们的数据清洗操作
 */
object SparkStatCleanJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkStatCleanJob")
      .master("local[2]").getOrCreate()

    val accessRDD = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/datashare/access.log")
    //accessRDD.take(10).foreach(println)
    //RDD ==> DF
    val accessDF = spark.createDataFrame(accessRDD.map(x => AccessConvertUtil.parseLog(x)),
      AccessConvertUtil.struct)

//    accessDF.printSchema()
//    accessDF.show(false)
    // 生成一个文件 覆写
    accessDF.coalesce(1).write.format("parquet")
      .mode(SaveMode.Overwrite)
      .partitionBy("day")
      .save("file:///Users/wangfulin/bigdata/data/datashare/clean")

    spark.stop()
  }
}
