package com.tz.stomp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

/**
 * @author tz
 * @Classname MyStompSessionHandler
 * @Description
 * @Date 2019-12-27 16:30
 */
@Slf4j
public class MyStompSessionHandler implements StompSessionHandler {
    /**
     * 要发送的对象，将会json化传输出去
     */
    private Object toSend;

    /**
     * 目的地，一般是topic地址
     */
    private String dest;

    public MyStompSessionHandler(String dest, Object toSend) {
        this.toSend = toSend;
        this.dest = dest;
    }
    /**
     * Invoked when the session is ready to use, i.e. after the underlying
     * transport (TCP, WebSocket) is connected and a STOMP CONNECTED frame is
     * received from the broker.
     *
     * @param session          the client STOMP session
     * @param connectedHeaders the STOMP CONNECTED frame headers
     */
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        log.info("MyStompSessionHandler---------->[afterConnected],sessionId=[{}]",session.getSessionId());
        session.send(dest, toSend);
//        session.send(connectedHeaders,"测试？？？");
        log.info("链接成功发送消息");
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                log.info("MyStompSessionHandler------->getPayloadType----->queue = [测试消息读取]--->header=[{}]",headers.getId());
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                // ...监听消息时注意程序不要停止，可以debug在上面停住，然后放开，就可以看到消息
                log.info("MyStompSessionHandler------->handleFrame----->queue = [测试消息读取] header=[{}],patLoad=[{}]",headers.getId(),payload);
            }

        });

    }

    /**
     * Handle any exception arising while processing a STOMP frame such as a
     * failure to convert the payload or an unhandled exception in the
     * application {@code StompFrameHandler}.
     *
     * @param session   the client STOMP session
     * @param command   the STOMP command of the frame
     * @param headers   the headers
     * @param payload   the raw payload
     * @param exception the exception
     */
    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("MyStompSessionHandler---------->[handleException],sessionId=[{}]",session.getSessionId());
        log.info("MyStompSessionHandler---------->[handleException],headId=[{}]",headers.getId());
        log.info("MyStompSessionHandler---------->[handleException],err=[{}]",exception.getMessage());

    }

    /**
     * Handle a low level transport error which could be an I/O error or a
     * failure to encode or decode a STOMP message.
     * <p>Note that
     * {@link ConnectionLostException
     * ConnectionLostException} will be passed into this method when the
     * connection is lost rather than closed normally via
     * {@link StompSession#disconnect()}.
     *
     * @param session   the client STOMP session
     * @param exception the exception that occurred
     */
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("MyStompSessionHandler---------->[handleTransportError],sessionId=[{}],err=[{}]",session.getSessionId(),exception.getMessage());

    }

    /**
     * Invoked before {@link #handleFrame(StompHeaders, Object)} to determine the
     * type of Object the payload should be converted to.
     *
     * @param headers the headers of a message
     */
    @Override
    public Type getPayloadType(StompHeaders headers) {
        log.info("MyStompSessionHandler---------->[getPayloadType],headers=[{}]",headers.getHeartbeat());

        return null;
    }

    /**
     * Handle a STOMP frame with the payload converted to the target type returned
     * from {@link #getPayloadType(StompHeaders)}.
     *
     * @param headers the headers of the frame
     * @param payload the payload, or {@code null} if there was no payload
     */
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("MyStompSessionHandler---------->[handleFrame],headId=[{}]",headers.getId());


    }
}
