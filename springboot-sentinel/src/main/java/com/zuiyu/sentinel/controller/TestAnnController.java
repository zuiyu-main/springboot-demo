package com.zuiyu.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zuiyu.sentinel.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName TestAnnController
 * @Description
 * @Date 13:04 2022/6/12
 **/
@RestController
public class TestAnnController {
    @Autowired
    AsyncService asyncService;

    @SentinelResource(value = "Sentinel_Ann", blockHandler = "exceptionHandler")
    @GetMapping("/ann")
    public String hello() {

        return "Hello Sentinel";
    }

    public void exceptionHandler(BlockException e) {
        e.printStackTrace();
        System.out.println("系统繁忙，请稍后");
    }

}
