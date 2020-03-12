package com.tz.springbootrabbitmq;

import com.alibaba.druid.support.json.JSONUtils;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class SpringbootRabbitmqApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void shutdownTest() throws IOException, TimeoutException {

    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("my_vhost");
        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
        connection.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println(JSONUtils.toJSONString(cause));
            }
        });
        connection.removeShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException e) {
                System.out.println(JSONUtils.toJSONString(e));

            }
        });

//        channel.queueDeclare("hello", false, false, false, null);
//        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + message + "'");
//        };
//        channel.basicConsume("hello", true, deliverCallback, consumerTag -> { });
    }
}
