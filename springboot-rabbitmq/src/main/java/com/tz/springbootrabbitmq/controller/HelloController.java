package com.tz.springbootrabbitmq.controller;

import com.tz.springbootrabbitmq.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author liBai
 * @Classname HelloController
 * @Description TODO
 * @Date 2019-06-02 10:49
 */
@RestController
@RequestMapping("/test")
public class HelloController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/count")
    public Object count() throws IOException {
        return helloService.count();
    }

}
