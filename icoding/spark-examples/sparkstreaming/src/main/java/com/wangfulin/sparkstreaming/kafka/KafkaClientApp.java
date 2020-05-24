package com.wangfulin.sparkstreaming.kafka;

/**
 * @projectName: sparkstreaming
 * @description: Kafka Java API测试
 * @author: Wangfulin
 * @create: 2020-05-21 10:12
 **/
public class KafkaClientApp {

    public static void main(String[] args) {
        new KafkaProducer(KafkaProperties.TOPIC).start();

        new KafkaConsumer(KafkaProperties.TOPIC).start();

    }
}
