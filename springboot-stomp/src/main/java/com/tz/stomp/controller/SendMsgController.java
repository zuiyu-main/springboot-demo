package com.tz.stomp.controller;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * @author tz
 * @Classname SendMsgController
 * @Description
 * @Date 2019-12-25 09:53
 */
@RestController
public class SendMsgController {
    @GetMapping(value="/greetings")
    public void greet() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        ThreadPoolTaskScheduler task=new ThreadPoolTaskScheduler();
        task.initialize();
        stompClient.setTaskScheduler(task);
//        final MyStompSessionHandler sessionHandler =
//                new MyStompSessionHandler();
//
//        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
//        headers.setSecWebSocketProtocol("13");
//
//        //连接用户名/密码也是必须的，否则连不上
//        StompHeaders sHeaders = new StompHeaders();
//        sHeaders.add("login", "admin");
//        sHeaders.add("passcode", "admin");
        String url = "ws://127.0.0.1:9020/sockjs";
//
//        //开始连接，回调连接上后发送stomp消息
//        stompClient.connect(url, headers, sHeaders, sessionHandler);
        StompSessionHandler sessionHandler = new MyStompSessionHandler("/exchange/test/test.*","我是测试消息啊");
        stompClient.connect(url, sessionHandler);

    }
}
