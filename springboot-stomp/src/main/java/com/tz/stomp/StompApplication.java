package com.tz.stomp;

import io.netty.channel.group.DefaultChannelGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.scheduler.Schedulers;
import reactor.netty.resources.LoopResources;

@SpringBootApplication
public class StompApplication {
    public static void main(String[] args) {
        SpringApplication.run(StompApplication.class, args);
    }
}
