package com.wangfulin.datastreamapi;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * @projectName: flink-examples
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-07-08 10:05
 **/
public class MyDataStreamSource implements SourceFunction<MyDataStreamSource.Item> {

    /**
    * @Name: run
    * @Description:  重写run方法产生一个源源不断的数据发送源
    * @Param: [ctx]
    * @return: void
    * @Author: Wangfulin
    * @Date: 2020/7/8
    */

    @Override
    public void run(SourceContext<Item> ctx) throws Exception {
        while (true) {
            Item item = generateItem();
            ctx.collect(item);

            // 每秒产生一条数据
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {

    }

    //随机产生一条商品数据
    private Item generateItem() {
        int i = new Random().nextInt(100);

        Item item = new Item();
        item.setName("name" + i);
        item.setId(i);
        return item;
    }


    class Item {
        private String name;
        private Integer id;

        public Item() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}
