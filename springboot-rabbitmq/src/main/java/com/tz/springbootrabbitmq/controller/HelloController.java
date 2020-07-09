package com.tz.springbootrabbitmq.controller;

import com.tz.springbootrabbitmq.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
    public String hello(HttpServletRequest request){
        return helloService.sayHello(request);
    }
    @RequestMapping("/get")
    public String getRedisInfo(){
        return helloService.getRedisInfo();
    }
    @GetMapping("/sendMsg")
    public String sendMsg() throws InterruptedException, ExecutionException, TimeoutException {
        return helloService.sendMsg();
    }





}
