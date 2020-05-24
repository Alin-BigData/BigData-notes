package org.wangfulin.graph

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 创建图 根据有属性的顶点和边构建
 **/
object CreateGraph1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("mySpark").setMaster("local[2]")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    // 顶点rdd  RDD[顶点的 ID,顶点的属性值]
    val users: RDD[(VertexId, (String, String))] =
      sc.textFile("/Users/wangfulin/bigdata/data/graph/vertices.txt").map(line => {
        val lines = line.split(" ")
        (lines(0).toLong, (lines(1), lines(2)))
      })

    // 边rdd RDD[起始点 ID,目标点 ID，边的属性（边的标注,边的权重等）] Edge类包含起点ID 目标ID 边属性三部分
    val relationships: RDD[Edge[String]] =
      sc.textFile("/Users/wangfulin/bigdata/data/graph/edges.txt").map(line => {
        val lines = line.split(" ")
        Edge(lines(0).toLong, lines(1).toLong, lines(2))
      })

    // 定义一个默认（缺失）用户
    val defaultUser = ("John Doe", "Missing")
    //使用 RDDs 建立一个 Graph
    val graph_urelate = Graph(users, relationships, defaultUser)


  }
}
