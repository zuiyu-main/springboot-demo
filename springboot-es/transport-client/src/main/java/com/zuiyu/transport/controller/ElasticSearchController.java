package com.zuiyu.transport.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName ElasticSearchController
 * @Description 测试Es Api
 * @Date 17:05 2021/4/13
 **/
@RestController
@RequestMapping("/es")
public class ElasticSearchController {
    @PostMapping("/index")
    public Object index() {
        return null;
    }
}
