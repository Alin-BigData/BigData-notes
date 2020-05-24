package com.wangfulin.spark
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
 * HiveContext的使用
 * 使用时需要通过--jars 把mysql的驱动传递到classpath
 */
object HiveContextApp {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    //在测试或者生产中，AppName和Master我们是通过脚本进行指定
    //sparkConf.setAppName("HiveContextApp").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)
    val hiveContext = new HiveContext(sc)

    //2)相关的处理:
    hiveContext.table("emp").show

    //3)关闭资源
    sc.stop()
  }

}
