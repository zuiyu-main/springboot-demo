package com.tz.tkmapper.controller;

import com.tz.tkmapper.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/hello")
    public String hello() {
        return helloService.sayHello();
    }

    @RequestMapping("/get")
    public String getRedisInfo() {
        return helloService.getRedisInfo();
    }

    @GetMapping("/")
    public void test() {
        helloService.testTransactional();
    }
}
