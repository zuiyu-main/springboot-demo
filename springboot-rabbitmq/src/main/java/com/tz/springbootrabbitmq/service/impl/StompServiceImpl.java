package com.tz.springbootrabbitmq.service.impl;


import com.tz.springbootrabbitmq.handler.CustomStompSessionHandler;
import com.tz.springbootrabbitmq.service.StompService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tz
 * @Classname StompServiceImpl
 * @Description
 * @Date 2019-09-18 18:45
 */
@Service
public class StompServiceImpl implements StompService {
    private static final String URL_TEMPLATE = "http://%s:%s/stomp";

    @Value("${spring.rabbitmq.host}")
    private String host;

    //@Value("${rabbit.stomp.port}")
    private Integer port = 15674;

    /**
     * 连接用户名
     */
    //@Value("${rabbit.stomp.login}")
    private String login = "admin";
    /**
     * 连接密码
     */
    //@Value("${rabbit.stomp.passCode}")
    private String passCode = "admin";

    private String url;

    @PostConstruct
    public void init()
    {
        url = String.format(URL_TEMPLATE, host, port);
    }

    /**
     * 发送stomp消息
     * @param dest  目的地 比如/topic/test
     * @param toSend  要发送的信息
     * @param <T>
     */
    @Override
    public <T> void connectAndSend(String dest, T toSend) {
        WebSocketClient client = new StandardWebSocketClient();

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport( client) );
        //rabbitmq 3.7以后就别这么写了。直接new WebSocketStompClient(client)就行
        WebSocketClient transport = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
        //StompSessionHandlerAdapter默认的payload类型是String， 因此MessageConverter必须是StringMessageConverter
        stompClient.setMessageConverter(new StringMessageConverter());

        final CustomStompSessionHandler sessionHandler =
                new CustomStompSessionHandler(dest, toSend);

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.setSecWebSocketProtocol("13");

        //连接用户名/密码也是必须的，否则连不上
        StompHeaders sHeaders = new StompHeaders();
        sHeaders.add("login", this.login);
        sHeaders.add("passcode", this.passCode);

        //开始连接，回调连接上后发送stomp消息
        stompClient.connect(url, headers, sHeaders, sessionHandler);

        //要同步得到发送结果的话，用CountDownLatch来做或者connect结果的future对象做get
    }

}
