package com.wangfulin.project

import com.wangfulin.project.dao.{CourseClickCountDAO, CourseSearchClickCountDAO}
import com.wangfulin.project.domain.{ClickLog, CourseClickCount, CourseSearchClickCount}
import com.wangfulin.project.utils.DateUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

import scala.collection.mutable.ListBuffer

/**
 * 使用Spark Streaming处理Kafka过来的数据
 */
object StatStreamingApp {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      println("Usage: StatStreamingApp <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }
    val Array(zkQuorum, groupId, topics, numThreads) = args

    val sparkConf = new SparkConf().setAppName("StatStreamingApp").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(60))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap

    val messages = KafkaUtils.createStream(ssc, zkQuorum, groupId, topicMap)

    // 测试步骤一：测试数据接收
    //messages.map(_._2).count().print


    // 测试步骤二：数据清洗
    val logs = messages.map(_._2)
    val cleanData = logs.map(line => {
      var courseId = 0
      val infos = line.split("\t")

      if (infos.length == 5) {
        //82.6.45.34	2020-05-23 23:24:00	"GET /class/141.html HTTP/1.1"	404	-

        // 82.88.45.123	2020-05-23 19:06:00	"GET /class/112.html HTTP/1.1"	404	http://www.sogou.com/web?query=hadoop
        // infos(2) = "GET /class/130.html HTTP/1.1"
        // url = /class/130.html
        // infos:,"GET /class/141.html HTTP/1.1"   404   -
        // "GET /class/141.html HTTP/1.1"	200	http://cn.bing.com/search?q=mysql
        var url = ""

        url = infos(2).split(" ")(1)


        if (url.startsWith("/class")) {
          val courseIdHtml = url.split("/")(2)
          courseId = courseIdHtml.substring(0, courseIdHtml.lastIndexOf(".")).toInt
        }
        ClickLog(infos(0), DateUtils.parseToMinute(infos(1)), courseId, infos(3).toInt, infos(4))
        // 返回值
      } else {
        ClickLog(infos(0), DateUtils.parseToMinute("2017-10-22 14:46:0"), 0, 0, "0")
      }

    }).filter(clicklog => clicklog.courseId != 0)

    cleanData.print()
    // 测试步骤三：统计今天到现在为止实战课程的访问量
    // 将cleanData 中的 ClickLog(1.64.67.77,20200523202600,143,500,-)
    // 转为 HBase rowkey设计： 20171111_88 前半截是时间日期 ，后半截是 课程编号

    cleanData.map(x => {
      (x.time.substring(0, 8) + "_" + x.courseId, 1)
    }).reduceByKey(_ + _)
      //这里是DStream 把DStream的数据按rdd partitionRecords加进去
      .foreachRDD(rdd => {
        rdd.foreachPartition(partitionRecords => {
          val list = new ListBuffer[CourseClickCount]
          partitionRecords.foreach(pair => {
            list.append(CourseClickCount(pair._1, pair._2))
          })
          // 按partitionRecords添加
          CourseClickCountDAO.save(list)
        })
      })

    // 测试步骤四：统计从搜索引擎过来的今天到现在为止实战课程的访问量
    /**
     * https://www.sogou.com/web?query=Spark SQL实战
     * - >
     * https:/www.sogou.com/web?query=Spark SQL实战
     */

    cleanData.map(x => {
      val refer = x.refer.replaceAll("//", "/")
      val splits = refer.split("/")
      var host = ""
      if (splits.length > 2) {
        host = splits(1)
      }
      // 返回的数据
      (host, x.courseId, x.time)
    }).filter(_._1 != "") // host不为空
      .map(x => {
        (x._3.substring(0, 8) + "_" + x._1 + "_" + x._2, 1)
      }).reduceByKey(_ + _)
      .foreachRDD(rdd => {
        rdd.foreachPartition(partitionRecords => {
          val list = new ListBuffer[CourseSearchClickCount]
          partitionRecords.foreach(pair => {
              list.append(CourseSearchClickCount(pair._1, pair._2))
          })
          CourseSearchClickCountDAO.save(list)
        })
      })


    ssc.start()
    ssc.awaitTermination()
  }

}
