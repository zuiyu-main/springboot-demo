package com.tz.springbootelasticsearch7.service.impl;


import com.tz.springbootelasticsearch7.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author liBai
 * @Classname HelloServiceImpl
 * @Description TODO
 * @Date 2019-06-02 10:50
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        return "";
    }

    @Override
    public String getRedisInfo() {
        return "";
    }
}
