package com.tz.springbootkafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author tz
 * @Classname KafkaProducer
 * @Description 生产者
 * @Date 2019-11-06 17:00
 */
@Slf4j
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String topic,String key,String msg) {
        this.kafkaTemplate.send(topic,key,msg);
        log.info("发送普通消息，topic={},key={},msg={}",topic,key,msg);
    }
    public void send(String topic,Integer partition ,String key,String msg) {
        this.kafkaTemplate.send(topic,partition,key,msg);
        log.info("发送普通消息，topic={},key={},msg={}",topic,key,msg);
    }
}
