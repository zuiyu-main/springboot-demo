package com.zuiyu.sentinel.controller;

import com.alibaba.csp.sentinel.SphO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName TestBooleanController
 * @Description
 * @Date 13:04 2022/6/12
 **/
@RestController
public class TestBooleanController {
    @GetMapping("/boolean")
    public boolean hello() {
        if (SphO.entry("Sentinel_Boolean")) {
            try {
                System.out.println("Hello Sentinel");
                return true;
            } finally {
                SphO.exit();
            }
        } else {
            // 限流降级的处理
            System.out.println("系统繁忙，请稍后");
            return false;
        }
    }

}
