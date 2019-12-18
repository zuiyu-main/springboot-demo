package com.tz.springbootrabbitmq.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liBai
 * @Classname HelloService
 * @Description TODO
 * @Date 2019-06-02 10:49
 */
public interface HelloService {
    String sayHello(HttpServletRequest request);

    String getRedisInfo();

    String sendMsg();

}
