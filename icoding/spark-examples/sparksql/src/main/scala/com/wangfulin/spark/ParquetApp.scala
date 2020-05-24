package com.wangfulin.spark

import org.apache.spark.sql.SparkSession

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
