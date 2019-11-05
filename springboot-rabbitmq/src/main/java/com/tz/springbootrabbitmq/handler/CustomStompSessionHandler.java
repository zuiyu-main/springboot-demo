package com.tz.springbootrabbitmq.handler;

import com.alibaba.druid.support.json.JSONUtils;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/**
 * @author tz
 * @Classname CustomStompSessionHandler
 * @Description
 * @Date 2019-09-18 18:51
 */
@Slf4j
public class CustomStompSessionHandler  extends StompSessionHandlerAdapter {
    /**
     * 要发送的对象，将会json化传输出去
     */
    private Object toSend;

    /**
     * 目的地，一般是topic地址
     */
    private String dest;

    public CustomStompSessionHandler(String dest, Object toSend) {
        this.toSend = toSend;
        this.dest = dest;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        super.handleFrame(headers, payload);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        super.afterConnected(session, connectedHeaders);
        String msg = JSONUtils.toJSONString(toSend);
        try{
            session.send(dest, msg);
        }catch(Exception e)
        {
            log.error("failed to send stomp msg({}) to destination {}", msg, dest);
        }finally {
            //做完了关闭呗
            session.disconnect();
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
        log.error("stomp error: {}", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        super.handleTransportError(session, exception);
        log.error("stomp transport error: {}", exception);
    }

    public void setToSend(Object toSend) {
        this.toSend = toSend;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
