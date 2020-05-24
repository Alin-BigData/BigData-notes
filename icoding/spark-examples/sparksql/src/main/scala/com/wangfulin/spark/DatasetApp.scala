package com.wangfulin.spark

import org.apache.spark.sql.SparkSession

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
