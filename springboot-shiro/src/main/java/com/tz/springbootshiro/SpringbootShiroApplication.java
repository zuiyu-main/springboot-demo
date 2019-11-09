package com.tz.springbootshiro;

import com.tz.springbootshiro.controller.TestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootShiroApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootShiroApplication.class, args);

        // Grab the 'QuickStart' bean, call 'run()' to start the example.
        context.getBean(TestController.class).run();
    }

}
