package com.wangfulin.sale

import com.wangfulin.sale.utils.{tbDateConvertUtil, tbStockConvertUtil, tbStockDetailConvertUtil}
import org.apache.spark.sql.SparkSession

object CompanySaleJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("CompanySaleJob")
      .master("local[2]").getOrCreate()

    val tbDateRDD = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/sale/tbDate.txt")
    val tbStockRDD = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/sale/tbStock.txt")
    val tbStockDetailRDD = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/sale/tbStockDetail.txt")


    //saleRDD.take(10).foreach(println)
    val tbDateDF = spark.createDataFrame(tbDateRDD.map(x => tbDateConvertUtil.parseLog(x)),
      tbDateConvertUtil.struct) //.show(false)

    val tbStockDF = spark.createDataFrame(tbStockRDD.map(x => tbStockConvertUtil.parseLog(x)),
      tbStockConvertUtil.struct) //.show(false)

    val tbStockDetailDF = spark.createDataFrame(tbStockDetailRDD.map(x => tbStockDetailConvertUtil.parseLog(x)),
      tbStockDetailConvertUtil.struct) //.show(false)

    // 创建临时表
    tbDateDF.createOrReplaceTempView("tbDate")
    tbStockDF.createOrReplaceTempView("tbStock")
    tbStockDetailDF.createOrReplaceTempView("tbStockDetail")


    // 计算所有订单中每年的销售单数和销售总额
    spark.sql("select tbDate.year,sum(IFNULL(tbStockDetail.qty, 0)) AS total_qty, sum(IFNULL(tbStockDetail.amount, 0)) AS total_amount from  tbDate  left join tbStock on tbDate.dateId = tbStock.dateId " +
      "left join tbStockDetail on tbStock.orderId = tbStockDetail.orderId group by tbDate.year"
    ).show()

    // 所有订单中每年 最大金额订单的销售量
    spark.sql("select tbDate.year, max(IFNULL(tbStockDetail.amount, 0)) AS total_amount from  tbDate  left join tbStock on tbDate.dateId = tbStock.dateId " +
      "left join tbStockDetail on tbStock.orderId = tbStockDetail.orderId group by tbDate.year"
    ).show()

  }

}
