package com.tz.tkmapper.service.impl;

import com.tz.tkmapper.bean.TestSys;
import com.tz.tkmapper.dao.TestSysMapper;
import com.tz.tkmapper.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private TestSysMapper testSysMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String sayHello() {
        TestSys testSys = testSysMapper.selectByPrimaryKey("1");
        redisTemplate.opsForValue().set("testSys",testSys.toString(),10,TimeUnit.MINUTES);
        log.info("redis set value =" +testSys.toString());
        return "redis set value success ，userName = "+testSys.getName();
    }

    @Override
    public String getRedisInfo() {
        log.debug("redis get info {}",redisTemplate.opsForValue().get("testSys"));
        return redisTemplate.opsForValue().get("testSys");
    }
}
