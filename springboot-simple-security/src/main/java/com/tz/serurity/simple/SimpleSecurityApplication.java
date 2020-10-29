package com.tz.serurity.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author cxt
 */
@SpringBootApplication
@ComponentScan("com.tz.serurity")
public class SimpleSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSecurityApplication.class, args);
    }

}
