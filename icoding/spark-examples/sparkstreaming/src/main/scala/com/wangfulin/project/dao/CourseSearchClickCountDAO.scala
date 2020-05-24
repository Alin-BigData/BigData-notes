package com.wangfulin.project.dao

import com.wangfulin.project.domain.{CourseClickCount, CourseSearchClickCount}
import com.wangfulin.sparkstreaming.project.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/**
 * 从搜索引擎过来的实战课程点击数-数据访问层
 */
object CourseSearchClickCountDAO {
  val tableName = "course_search_clickcount"
  val cf = "info"
  // 存储点击的count
  val qualifer = "click_count"

  /**
   * 保存数据到HBase
   * 一串数据一起保存
   *
   * @param list CourseSearchClickCount
   */
  def save(list: ListBuffer[CourseSearchClickCount]): Unit = {
    val table = HBaseUtils.getInstance.getTable(tableName)
    for (ele <- list) {
      // 如果数据库里面有这个数据，则取出来加上当前传入的值
      // row_key 对应的cf 列 qualifer 的数量加上来
      // public Long incrementColumnValue(
      // row: Array[Byte],
      // family: Array[Byte],
      // qualifier: Array[Byte],
      // amount: Long) throws IOException {

      table.incrementColumnValue(
        Bytes.toBytes(ele.day_search_course),
        Bytes.toBytes(cf),
        Bytes.toBytes(qualifer),
        ele.click_count
      )
    }

  }

  /**
   * 根据rowkey查询值
   */
  def count(day_search_course: String): Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    val key = new Get(Bytes.toBytes(day_search_course))
    val value = table.get(key).getValue(Bytes.toBytes(cf), Bytes.toBytes(qualifer))
    // val value = table.get(key).getValue(cf.getBytes,qualifer.getBytes) //这个也可以


    if (value == null) {
      0L
    } else {
      Bytes.toLong(value)
    }

  }

  def main(args: Array[String]): Unit = {
    val list = new ListBuffer[CourseSearchClickCount]
    list.append(CourseSearchClickCount("20171111_www.baidu.com_8", 8))
    list.append(CourseSearchClickCount("20171111_www.baidu.com_9", 9))
    list.append(CourseSearchClickCount("20171111_www.baidu.com_1", 100))
    save(list)
    println(count("20171111_www.baidu.com_8"))

    //println(count("20171111_8") + " : " + count("20171111_9") + " : " + count("20171111_1"))
  }

}
