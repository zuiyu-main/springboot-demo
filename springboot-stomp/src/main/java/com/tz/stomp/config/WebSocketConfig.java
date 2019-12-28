package com.tz.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author tz
 * @Classname WebSocketConfig2
 * @Description
 * @Date 2019-12-24 17:10
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        // app 订阅基础路径
//        config.setApplicationDestinationPrefixes("/app");
//        config.enableStompBrokerRelay("/queue", "/topic")
//                .setRelayHost("127.0.0.1")
//                .setRelayPort(61613)
//                .setVirtualHost("my_vhost")
//                .setClientLogin("admin")
//                .setClientPasscode("admin");
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // sockjs路径url
        registry.addEndpoint("/sockjs");
//                .withSockJS()
//                .setStreamBytesLimit(512 * 1024)
//                .setHttpMessageCacheSize(1000)
//                .setDisconnectDelay(30 * 1000);
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MyChannelInterceptor());
    }
}
