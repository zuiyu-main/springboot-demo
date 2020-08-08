package com.tz.springbootrabbitmq.rabbit;

import com.tz.springbootrabbitmq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author tz
 * @Classname MsgProducer
 * @Description 生产者
 * @Date 2019-09-18 17:40
 */
@Component
@Slf4j
public class MsgProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate = rabbitTemplate;

        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }

    /**
     * 发送普通消息
     *
     * @param exchange 交换机
     * @param routeKey 路由key
     * @param msg      发送的消息
     */
    public void sendMsg(String exchange, String routeKey, Object msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routeKey, msg, correlationId);
    }

    /**
     * 发送优先级消息
     * 前提： 队列支持优先级
     *
     * @param exchange 交换机
     * @param routeKey 路由key
     * @param msg      消息内容
     * @param priority 优先级
     */
    public void sendPriorityMsg(String exchange, String routeKey, Object msg, Integer priority) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //设置编码
            messageProperties.setContentEncoding("utf-8");
            messageProperties.setPriority(priority);
            return message;
        };
        rabbitTemplate.convertAndSend(exchange, routeKey, msg, messagePostProcessor, correlationId);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        log.info("MsgProducer confirm correlationData: [{}]" , correlationData);
        if (b) {
            log.info("消息成功消费 correlationData: [{}]",correlationData);
        } else {
            log.info("消息消费失败:[{}]" ,s);
        }
    }


    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("MsgProducer returnedMessage message: [{}]", new String(message.getBody()));
        log.info("MsgProducer returnedMessage replyCode: [{}]", replyCode);
        log.info("MsgProducer returnedMessage replyText: [{}]", replyText);
        log.info("MsgProducer returnedMessage exchange: [{}]", exchange);
        log.info("MsgProducer returnedMessage routingKey: [{}]", routingKey);
    }

    @Scheduled(cron = "0/5 * *  * * ? ")
    public void autoSendMsg() {
//        sendMsg(RabbitConfig.TEST_EXCHANGE,RabbitConfig.TEST_ROUTE_KEY,UUID.randomUUID().toString());


        sendMsg(RabbitConfig.DEFAULT_EXCHANGE, RabbitConfig.DEFAULT_ROUTE_KEY, UUID.randomUUID().toString());
    }


}
