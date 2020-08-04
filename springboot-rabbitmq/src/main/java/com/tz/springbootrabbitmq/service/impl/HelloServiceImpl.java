package com.tz.springbootrabbitmq.service.impl;

import com.tz.springbootrabbitmq.dao.TestSysMapper;
import com.tz.springbootrabbitmq.rabbit.MsgProducer;
import com.tz.springbootrabbitmq.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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



}
