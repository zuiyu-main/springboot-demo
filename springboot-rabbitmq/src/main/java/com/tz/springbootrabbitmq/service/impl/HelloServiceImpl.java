package com.tz.springbootrabbitmq.service.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.tz.springbootrabbitmq.dao.TestSysMapper;
import com.tz.springbootrabbitmq.rabbit.MsgProducer;
import com.tz.springbootrabbitmq.service.HelloService;
import com.tz.springbootrabbitmq.util.RabbitUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.tz.springbootrabbitmq.config.RabbitConfig.*;

/**
 * @author liBai
 * @Classname HelloServiceImpl
 * @Description TODO
 * @Date 2019-06-02 10:50
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Autowired
    private TestSysMapper testSysMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MsgProducer msgProducer;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitUtils rabbitUtils;


    @Override
    public Map<String, Object> count() throws IOException {
        int messageCount = rabbitUtils.getMessageCount(TEST_QUEUE_2);
        Map<String, Object> res = new HashMap<>();
        res.put("unconfirmedCount", messageCount);
        return res;
    }
}
