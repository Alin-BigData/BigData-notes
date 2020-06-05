package com.wangfulin.tb

import org.apache.spark.sql.SparkSession

object TbSql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("TbSql").master("local[2]").getOrCreate()
    val tbStockRdd = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/tb/tbStock.txt")
    val tbStockDetailRdd = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/tb/tbStockDetail.txt")
    val tbDateRdd = spark.sparkContext.textFile("file:///Users/wangfulin/bigdata/data/tb/tbDate.txt")

    //注意：需要导入隐式转换
    import spark.implicits._

    val tbStockDS = tbStockRdd.map(_.split(",")).map(attr => {
      tbStock(attr(0), attr(1), attr(2))
    }).toDS()

    val tbStockDetailDS = tbStockDetailRdd.map(_.split(",")).map(attr => {
      tbStockDetail(attr(0), attr(1).trim.toInt, attr(2), attr(3).trim.toInt, attr(4).trim.toDouble, attr(5).trim.toDouble)
    }).toDS()

    val tbDateDS = tbDateRdd.map(_.split(",")).map(attr => {
      tbDate(attr(0), attr(1).trim.toInt, attr(2).trim.toInt, attr(3).trim.toInt, attr(4).trim.toInt, attr(5).trim.toInt, attr(6).trim.toInt, attr(7).trim.toInt, attr(8).trim.toInt, attr(9).trim.toInt)
    }).toDS()

    //    tbStockDS.show()
    //    tbStockDetailDS.show()
    //    tbDateDS.show()

    tbStockDS.createOrReplaceTempView("tbStock")
    tbDateDS.createOrReplaceTempView("tbDate")
    tbStockDetailDS.createOrReplaceTempView("tbStockDetail")

    // 计算所有订单中每年的销售单数、销售总额
    spark.sql("select dt.theyear,count(DISTINCT st.ordernumber),sum(sd.amount) from tbStock as st  join tbStockDetail sd " +
      "on st.ordernumber = sd.ordernumber  join tbDate dt on dt.dateid = st.dateid group by dt.theyear  ORDER BY dt.theyear").show
    // 计算所有订单每年最大金额订单的销售额
    spark.sql("select dt.theyear,max(sd.price * sd.number),sum(sd.amount) from tbStock as st  join tbStockDetail sd " +
      "on st.ordernumber = sd.ordernumber  join tbDate dt on dt.dateid = st.dateid group by dt.theyear  ORDER BY dt.theyear").show


  }

}

case class tbStock(ordernumber: String, locationid: String, dateid: String) extends Serializable

case class tbStockDetail(ordernumber: String, rownum: Int, itemid: String, number: Int, price: Double, amount: Double) extends Serializable

case class tbDate(dateid: String, years: Int, theyear: Int, month: Int, day: Int, weekday: Int, week: Int, quarter: Int, period: Int, halfmonth: Int) extends Serializable
