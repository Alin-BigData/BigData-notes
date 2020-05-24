package com.wangfulin.sale.utils

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

/**
 * 访问日志转换(输入==>输出)工具类
 */
object tbDateConvertUtil {
  // 定义结构体
  // 定义的输出的字段

  val struct = StructType(
    Array(
      StructField("dateId", StringType),
      StructField("yearMonth", StringType),
      StructField("year", StringType),
      StructField("month", StringType),
      StructField("week", StringType),
      StructField("weeks", StringType),
      StructField("quot", StringType),
      StructField("tenday", StringType),
      StructField("halfMonth", StringType)
    )
  )


  /**
   * 根据输入的每一行信息转换成输出的样式
   **/
  def parseLog(log: String) = {
    try {
      val splits = log.split("\\,")

      val dateId = splits(0)
      val yearMonth = splits(1)
      val year = splits(2)
      val month = splits(3)
      val week = splits(4)
      val weeks = splits(5)
      val quot = splits(6)
      val tenday = splits(7)
      val halfMonth = splits(8)

      //这个row里面的字段要和struct中的字段对应上
      Row(dateId, yearMonth, year, month, week, weeks, quot, tenday, halfMonth)
    } catch {
      case e: Exception => Row(0)

    }

  }
}
