package com.tz.springbootrabbitmq.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName MsgHandlerService
 * @Description 消息监听回调类
 * @Date 14:47 2020/6/30
 **/
@Component
@Slf4j
public class MsgHandlerService implements ChannelAwareMessageListener {
    /**
     * 1、处理成功，这种时候用basicAck确认消息；
     * 2、可重试的处理失败，这时候用basicNack将消息重新入列；
     * 3、不可重试的处理失败，这时候使用basicNack将消息丢弃。
     * <p>
     * basicNack(long deliveryTag, boolean multiple, boolean requeue)
     * deliveryTag:该消息的index
     * multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
     * requeue：被拒绝的是否重新入队列
     * <p>
     * Callback for processing a received Rabbit message.
     * <p>Implementors are supposed to process the given Message,
     * typically sending reply messages through the given Session.
     *
     * @param message the received AMQP message (never <code>null</code>)
     * @param channel the underlying Rabbit Channel (never <code>null</code>)
     * @throws Exception Any.
     */
    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        try {

            log.info("MsgHandlerService 线程 [{}] 监听到消息 [{}]", Thread.currentThread().getName(), new String(message.getBody()));
            int i = 1 / 0;
            log.info("i=[{}]", i);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.warn("MsgHandlerService 报错: [{}],[{}]", e.getMessage(), e.getStackTrace());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }
}
