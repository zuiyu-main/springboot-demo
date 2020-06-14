package com.tz.springbootrabbitmq.service.impl;

import com.tz.springbootrabbitmq.config.RabbitConfig;
import com.tz.springbootrabbitmq.dao.TestSysMapper;
import com.tz.springbootrabbitmq.rabbit.MsgProducer;
import com.tz.springbootrabbitmq.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

    @Override
    public String sayHello(HttpServletRequest request) {
        rabbitTemplate.convertAndSend("test","test.*", "test");
//        TestSys testSys = testSysMapper.selectByPrimaryKey("1");
//        redisTemplate.opsForValue().set("testSys",testSys.toString(),10,TimeUnit.MINUTES);
//        log.info("redis set value =" +testSys.toString());
//        return "redis set value success ，userName = "+testSys.getName();
        return "success";
    }

    @Override
    public String getRedisInfo() {
        log.debug("redis get info {}",redisTemplate.opsForValue().get("testSys"));
        return redisTemplate.opsForValue().get("testSys");
    }

    @Override
    public String sendMsg() {
//        // direct
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1", 1);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-2", 1);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-3", 1);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-4", 1);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-5", 1);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-5", 5);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-4", 5);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-3", 5);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-2", 5);
//        msgProducer.sendPriorityMsg(RabbitConfig.MY_EXCHANGE, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-1", 5);

        // topic
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1", 1);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-2", 1);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-3", 1);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-4", 1);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务1-5", 1);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-5", 5);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-4", 5);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-3", 5);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-2", 5);
        msgProducer.sendPriorityMsg(RabbitConfig.EXCHANGE_TEST, RabbitConfig.PRIORITY_ROUTE_KEY, "测试优先级任务2-1", 5);
//        for(int i=0;i<10;i++){
//            rabbitTemplate.convertAndSend("test","#","test"+i);
////            msgProducer.sendMsg("第"+i+"条信息，来吧宝贝，尽情的消费吧");
//        }
        return "success";
    }
}
