package com.wangfulin.sparkstreaming.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @projectName: sparkstreaming
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-21 10:02
 **/
public class KafkaProducer extends Thread {
    private String topic;

    private Producer<Integer, String> producer;

    // 构造
    public KafkaProducer(String topic) {
        this.topic = topic;

        Properties properties = new Properties();

        properties.put("metadata.broker.list", KafkaProperties.BROKER_LIST);
        // 序列化类型
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");

        producer = new Producer<Integer, String>(new ProducerConfig(properties));
    }

    // 测试
    @Override
    public void run() {

        int messageNo = 1;

        while (true) {
            String message = "message_" + messageNo;
            producer.send(new KeyedMessage<Integer, String>(topic, message));
            System.out.println("Sent: " + message);

            messageNo++;

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
