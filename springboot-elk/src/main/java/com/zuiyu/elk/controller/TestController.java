package com.zuiyu.elk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName TestController
 * @Description
 * @Date 19:41 2022/6/3
 **/
@RestController
public class TestController {
    public static final Logger log = LoggerFactory.getLogger(TestController.class);
    @RequestMapping("/test")
    public String test() {
        log.info("this is a log from springboot");
        log.trace("this is a trace log ");
        return "success";
    }
}
