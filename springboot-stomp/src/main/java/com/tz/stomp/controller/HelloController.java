package com.tz.stomp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author tz
 * @Classname HelloController
 * @Description
 * @Date 2019-12-25 09:32
 */
@Controller
public class HelloController {
    @MessageMapping("/greeting")
    public String handle(String greeting) {
        System.out.println(System.currentTimeMillis()+";;"+greeting);
        return "[" + System.currentTimeMillis() + ": " + greeting;
    }
}
