package com.tz.springbootrabbitmq.rabbit;

import com.rabbitmq.client.Channel;
import com.tz.springbootrabbitmq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author tz
 * @Classname MsgReceiver
 * @Description 消费者
 * @Date 2019-09-18 17:45
 */
@Component
@Slf4j
public class MsgReceiver {
    /**
     * 直接监听
     *
     * @param content
     */
    @RabbitListener(queues = RabbitConfig.PRIORITY_QUEUE)
    @RabbitHandler
    public void defaultListener(String content) {
        log.info("MsgReceiver 接收默认队列当中的消息：[{}] ", content);
    }

    /**
     * 自定义配置监听
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitConfig.TEST_QUEUE_1, durable = "true"),
            exchange = @Exchange(name = RabbitConfig.TEST_EXCHANGE, durable = "true", type = "topic"),
            key = RabbitConfig.TEST_ROUTE_KEY
    )
    )
    @RabbitHandler
//    @RabbitListener(queues = RabbitConfig.TEST_QUEUE_1)
    public void testListener(Message message, Channel channel) throws IOException {
        log.info("MsgReceiver 接收test1队列当中的消息：[{}]", new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
