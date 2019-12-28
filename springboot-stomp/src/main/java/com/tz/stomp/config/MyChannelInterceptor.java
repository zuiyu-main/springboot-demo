package com.tz.stomp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

/**
 * @author tz
 * @Classname MyChannelInterceptor
 * @Description
 * @Date 2019-12-27 16:28
 */
@Slf4j
public class MyChannelInterceptor implements ChannelInterceptor {
    /**
     * Invoked before the Message is actually sent to the channel.
     * This allows for modification of the Message if necessary.
     * If this method returns {@code null} then the actual
     * send invocation will not occur.
     *
     * @param message
     * @param channel
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("MyChannelInterceptor -------> [preSend],header=[{}]",message.getHeaders());
        return message;
    }

    /**
     * Invoked immediately after the send invocation. The boolean
     * value argument represents the return value of that invocation.
     *
     * @param message
     * @param channel
     * @param sent
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("MyChannelInterceptor ------->[postSend],header=[{}]",message.getHeaders());

    }

    /**
     * Invoked after the completion of a send regardless of any exception that
     * have been raised thus allowing for proper resource cleanup.
     * <p>Note that this will be invoked only if {@link #preSend} successfully
     * completed and returned a Message, i.e. it did not return {@code null}.
     *
     * @param message
     * @param channel
     * @param sent
     * @param ex
     * @since 4.1
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        log.info("MyChannelInterceptor ------->[afterSendCompletion],header=[{}]",message.getHeaders());

    }

    /**
     * Invoked as soon as receive is called and before a Message is
     * actually retrieved. If the return value is 'false', then no
     * Message will be retrieved. This only applies to PollableChannels.
     *
     * @param channel
     */
    @Override
    public boolean preReceive(MessageChannel channel) {
        log.info("MyChannelInterceptor ------->[preReceive]");


        return false;
    }

    /**
     * Invoked immediately after a Message has been retrieved but before
     * it is returned to the caller. The Message may be modified if
     * necessary; {@code null} aborts further interceptor invocations.
     * This only applies to PollableChannels.
     *
     * @param message
     * @param channel
     */
    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        log.info("MyChannelInterceptor ------->[postReceive],header=[{}]",message.getHeaders());

        return null;
    }

    /**
     * Invoked after the completion of a receive regardless of any exception that
     * have been raised thus allowing for proper resource cleanup.
     * <p>Note that this will be invoked only if {@link #preReceive} successfully
     * completed and returned {@code true}.
     *
     * @param message
     * @param channel
     * @param ex
     * @since 4.1
     */
    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        log.info("MyChannelInterceptor ------->[afterReceiveCompletion],header=[{}]",message.getHeaders());

    }
}
