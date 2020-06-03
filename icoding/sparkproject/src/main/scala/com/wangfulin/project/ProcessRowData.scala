package com.wangfulin.project

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode

object ProcessRowData {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    // 将json文件加载成一个dataframe
    val mealData = spark.read.format("json").load("/Users/wangfulin/bigdata/data/MealRatings.json")

    // 输出dataframe对应的schema信息
    mealData.printSchema()

    mealData.createOrReplaceTempView("mealData")

    val result = spark.sql("select * from mealData")

    result.take(5).toList.foreach(println)

    // 删除重复的评分数据
    // 获得最新评价的日期组合
    val lasterRatingPair = spark.sql("select UserID,MealID,MAX(ReviewTime) AS LastestDate from mealData " +
      "group by UserID,MealID")

    lasterRatingPair.createOrReplaceTempView("lasterRatingPair")
    val result1 = spark.sql("select * from lasterRatingPair")

    result1.take(5).toList.foreach(println)

    // 联表查询获得各用户最新的评分记录
    val lastestRatings = spark.sql("select a.UserID,a.MealID, a.Rating, a.ReviewTime from mealData a ," +
      "lasterRatingPair b where a.UserID = b.UserID and a.MealID = b.MealID and a.ReviewTime = b.LastestDate ")

    // 处理后的数据存储在本地
    val outputPath = "/Users/wangfulin/bigdata/data/cleanMealRatings.json"
    lastestRatings.write.json(outputPath)

    // 读取清洗后的数据
    val cleanMealData = spark.read.format("json").load(outputPath)

    // 选择属性 生成用户评分数据集
    val MealRatings = cleanMealData.select("UserID", "MealID", "Rating", "ReviewTime")
    val RatingRDD = MealRatings.map(line =>
      (line.getString(0), line.getString(1), line.getString(2), line.getLong(3)))


  }


}
