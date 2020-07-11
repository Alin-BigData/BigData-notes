package com.wangfulin.wordcount;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import java.util.ArrayList;

/**
 * @projectName: flink-examples
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-13 14:25
 **/
public class WordCountSQL {
    public static void main(String[] args) throws Exception {
        //获取运行环境
        ExecutionEnvironment fbEnv = ExecutionEnvironment.getExecutionEnvironment();

        //创建一个tableEnvironment
        BatchTableEnvironment fbTableEnv = BatchTableEnvironment.create(fbEnv);

        String text = "hello flink hello lagou";
        String[] words = text.split("\\W+");
        ArrayList<WC> wordList = new ArrayList<>();

        for (String word : words) {
            WC wc = new WC(word, 1);
            wordList.add(wc);
        }
        // 通过集合创建DataSet
        DataSet<WC> input = fbEnv.fromCollection(wordList);

        //DataSet 转sql, 指定字段名
        Table table = fbTableEnv.fromDataSet(input, "word,frequency");
        table.printSchema();

        // 注册为一个表
        fbTableEnv.createTemporaryView("WordCount", table);

        // 查询
        Table queryTable = fbTableEnv.sqlQuery("select word as word, sum(frequency) as frequency from WordCount GROUP BY word");

        //将表转换DataSet
        DataSet<WC> ds3 = fbTableEnv.toDataSet(queryTable, WC.class);
        ds3.printToErr();

    }


    public static class WC {
        public String word;
        public long frequency;

        public WC() {
        }

        public WC(String word, long frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return word + ", " + frequency;
        }
    }
}
