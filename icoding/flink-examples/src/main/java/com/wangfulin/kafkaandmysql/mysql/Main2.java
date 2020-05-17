package com.wangfulin.kafkaandmysql.mysql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @projectName: flink-examples
 * @description: 验证mysql例子
 * @author: Wangfulin
 * @create: 2020-05-17 10:24
 **/
public class Main2 {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new SourceFromMySQL()).print();

        env.execute("Flink add data sourc");
    }
}
