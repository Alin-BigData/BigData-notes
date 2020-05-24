package com.wangfulin.sale.utils

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
 * 访问日志转换(输入==>输出)工具类
 */
object tbStockDetailConvertUtil {
  // 定义结构体
  // 定义的输出的字段

  val struct = StructType(
    Array(
      StructField("orderId", StringType),
      StructField("row", StringType),
      StructField("itemId", StringType),
      StructField("qty", StringType),
      StructField("price", StringType),
      StructField("amount", StringType)
    )
  )


  /**
   * 根据输入的每一行信息转换成输出的样式
   **/
  def parseLog(log: String) = {
    try {
      val splits = log.split("\\,")

      val orderId = splits(0)
      val row = splits(1)
      val itemId = splits(2)
      val qty = splits(3)
      val price = splits(4)
      val amount = splits(5)

      //这个row里面的字段要和struct中的字段对应上
      Row(orderId, row, itemId, qty, price, amount)
    } catch {
      case e: Exception => Row(0)

    }

  }
}
