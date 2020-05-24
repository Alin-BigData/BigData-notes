package com.wangfulin

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}
import java.util

import scala.collection.mutable.Set
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 使用Spark Streaming完成词频统计，并将结果写入到MySQL数据库中
 */
object ForeachRDDAppPro {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("ForeachRDDApp").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))


    val lines = ssc.socketTextStream("localhost", 6789)

    val result = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print()


    var rs: ResultSet = null
    var stmt: PreparedStatement = null
    var supdate: PreparedStatement = null

    result.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {

        partitionOfRecords.foreach(record => {
          val connection = JDBCConnectePools.getConn()
          if (Remenber.keySet.contains(record._1)) {
            var preWordCount = 0
            var newWordCount = 0
            val querySql = "select wordcount from wordcount where word = ?"
            stmt = connection.prepareStatement(querySql)
            stmt.setString(1, record._1)
            rs = stmt.executeQuery()
            if (rs.next()) {
              preWordCount = rs.getInt("wordcount")
              newWordCount = preWordCount + record._2
            }
            val updateSql = "update wordcount set wordcount = ? where word = ?"
            supdate = connection.prepareStatement(updateSql)
            supdate.setInt(1, newWordCount)
            supdate.setString(2, record._1)
            supdate.executeUpdate()
          } else {
            Remenber.keySet += record._1
            val sql = "insert into wordcount(word, wordcount) values('" + record._1 + "'," + record._2 + ")"
            connection.createStatement().execute(sql)
          }
          JDBCConnectePools.returnConn(connection)

        })
      })
    })


    ssc.start()
    ssc.awaitTermination()
  }

  /**
   * 获取MySQL的连接
   */
/*  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://localhost:3306/imooc_project", "root", "123456")
  }*/
}

object Remenber{
  var keySet: Set[String] = Set()
}

object JDBCConnectePools {
  private val max = 10 //设置连接最大数
  private val ConnectionNum = 10 //设置 每次可以获取几个Connection
  private var conNum = 0 //连接数
  private val pool = new util.LinkedList[Connection]() //连接池

  def getDriver(): Unit = { //加载Driver
    //加载
    //这里判断的两个空不能去掉
    //可能有人以为在调用getDriver方法时已经判断过pool.isEmpty了，
    //在进行判断是没有意义的，而且当 连接数已经大于等于max时，会死循环
    //但是要考虑到运行过成中，在spark中是多线程运行的，在调用
    //getConnection方法时，可能当时pool中是空的，但是在调用后，
    //可能其他线程的数据运行完了，会还连接，
    //那么此时再进行判断时pool就不是空了，两个调教都不成立，
    //才能跳出循环，此时的情况是，获取的连接数已经大于等最大（max）的值
    //并且 已经有人把连接换了， 就可以直接取连接了，不用再创建
    //，也不能再创建
    if (conNum < max && pool.isEmpty) { //
      Class.forName("com.mysql.jdbc.Driver")

    } else if (conNum >= max && pool.isEmpty) {
      print("当前暂无可用Connection")
      Thread.sleep(2000)
      getDriver()
    }
  }

  def getConn(): Connection = {
    if (pool.isEmpty) {
      getDriver()
      for (i <- 1 to ConnectionNum) { //创建10个连接
        val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/imooc_project", "root", "123456")
        pool.push(conn) //  把连接放到连接池中，push是LinkedList中的方法
        conNum += 1
      }
    }
    val conn: Connection = pool.pop() //从线程池所在LinkedList中弹出一个Connection,pop 是LinkedList的方法
    conn //返回一个Connection
  }

  def returnConn(conn: Connection): Unit = { //还连接
    pool.push(conn)
  }

}
