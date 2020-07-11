package com.wangfulin.datastreamapi;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

/**
 * @projectName: flink-examples
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-07-08 10:46
 **/
public class DataStreamDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 获取数据源 并行度设置为1,
        DataStreamSource<MyDataStreamSource.Item> text =
                env.addSource(new MyDataStreamSource()).setParallelism(1);
        // map
//        DataStream<MyDataStreamSource.Item> item = text.map(
//                (MapFunction<MyDataStreamSource.Item, MyDataStreamSource.Item>) value -> value);


        // flatMap
//        SingleOutputStreamOperator<Object> item = text.flatMap(new FlatMapFunction<MyDataStreamSource.Item, Object>() {
//            @Override
//            public void flatMap(MyDataStreamSource.Item item, org.apache.flink.util.Collector<Object> collector) throws Exception {
//                String name = item.getName();
//                collector.collect(name);
//            }
//        });

        // filter
//        SingleOutputStreamOperator<MyDataStreamSource.Item> item = text.filter(new FilterFunction<MyDataStreamSource.Item>() {
//            @Override
//            public boolean filter(MyDataStreamSource.Item item) throws Exception {
//                return item.getId() % 2 == 0;
//            }
//        });
//        SingleOutputStreamOperator<MyDataStreamSource.Item> item = text.filter(
//                item1 -> item1.getId() % 2 == 0
//        );

        //获取数据源
        List data = new ArrayList<Tuple3<Integer, Integer, Integer>>();
        data.add(new Tuple3<>(0, 1, 0));
        data.add(new Tuple3<>(0, 1, 1));
        data.add(new Tuple3<>(0, 2, 2));
        data.add(new Tuple3<>(0, 1, 3));
        data.add(new Tuple3<>(1, 2, 5));
        data.add(new Tuple3<>(1, 2, 9));
        data.add(new Tuple3<>(1, 2, 11));
        data.add(new Tuple3<>(1, 2, 13));
//        DataStreamSource<MyDataStreamSource.Item> item = env.fromCollection(data);
//        // 按照 Tuple3 的第一个元素进行聚合，并且按照第三个元素取最大值。
//        item.keyBy(0).max(2).printToErr();
        DataStreamSource<Tuple3<Integer, Integer, Integer>> item = env.fromCollection(data);
        SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> reduceItem = item.keyBy(0).reduce(
                new ReduceFunction<Tuple3<Integer, Integer, Integer>>() {
                    @Override
                    public Tuple3<Integer, Integer, Integer> reduce(Tuple3<Integer, Integer, Integer> t1, Tuple3<Integer, Integer, Integer> t2) throws Exception {
                        Tuple3<Integer, Integer, Integer> newTuple = new Tuple3<>();
                        newTuple.setFields(0, 0, (Integer) t1.getField(2) + (Integer) t2.getField(2));
                        return newTuple;
                    }
                });

        reduceItem.printToErr();
        //打印结果
        item.print().setParallelism(1);
        String jobName = "user defined streaming source";
        env.execute(jobName);
    }
}
