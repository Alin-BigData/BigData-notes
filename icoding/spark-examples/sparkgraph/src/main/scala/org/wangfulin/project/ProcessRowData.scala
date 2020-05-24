package org.wangfulin.project

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object ProcessRowData {
  val sparkConf = new SparkConf()
  //在测试或者生产中，AppName和Master我们是通过脚本进行指定
  //sparkConf.setAppName("HiveContextApp").setMaster("local[2]")

  val sc = new SparkContext(sparkConf)
  val hiveContext = new HiveContext(sc)

  val inputPath = "/Users/wangfulin/bigdata/data/MealRatings.json"

  val mealData = hiveContext.jsonFile(inputPath)

  

}
