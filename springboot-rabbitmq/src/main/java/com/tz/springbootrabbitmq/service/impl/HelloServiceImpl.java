package com.tz.springbootrabbitmq.service.impl;

import com.tz.springbootrabbitmq.bean.TestSys;
import com.tz.springbootrabbitmq.dao.TestSysMapper;
import com.tz.springbootrabbitmq.rabbit.MsgProducer;
import com.tz.springbootrabbitmq.service.HelloService;
import com.tz.springbootrabbitmq.service.StompService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
    private StompService stompService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public String sayHello() {
        rabbitTemplate.convertAndSend("test","test.*","我是测试消息消息，测试广播");
        TestSys testSys = testSysMapper.selectByPrimaryKey("1");
        redisTemplate.opsForValue().set("testSys",testSys.toString(),10,TimeUnit.MINUTES);
        log.info("redis set value =" +testSys.toString());
        return "redis set value success ，userName = "+testSys.getName();
    }

    @Override
    public String getRedisInfo() {
        log.debug("redis get info {}",redisTemplate.opsForValue().get("testSys"));
        return redisTemplate.opsForValue().get("testSys");
    }

    @Override
    public String sendMsg() {
        for(int i=0;i<10;i++){
            msgProducer.sendMsg("第"+i+"条信息，来吧宝贝，尽情的消费吧");
        }
//        stompService.connectAndSend("/topic/test","{he:'hello'}");
        return "success";
    }
}
