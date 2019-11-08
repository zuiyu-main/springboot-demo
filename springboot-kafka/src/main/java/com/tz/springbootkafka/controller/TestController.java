package com.tz.springbootkafka.controller;

import com.tz.springbootkafka.constant.MyTopic;
import com.tz.springbootkafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tz
 * @Classname TestController
 * @Description
 * @Date 2019-11-07 11:36
 */
@RestController
public class TestController {
    @Autowired
    private KafkaProducer kafkaProducer;


    @GetMapping(path = "/test/{what}")
    public void sendTest(@PathVariable String what) {
        kafkaProducer.send(MyTopic.TEST,"test.*",what);
    }

    @GetMapping(path = "/topic1/{what}")
    public void sendTopic1(@PathVariable String what) {
        kafkaProducer.send(MyTopic.TOPIC1,3,"topic.*",what);
    }
}
