package com.zuiyu.sentinel.controller;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zuiyu.sentinel.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName TestAsyncController
 * @Description
 * @Date 13:04 2022/6/12
 **/
@RestController
public class TestAsyncController {
    @Autowired
    AsyncService asyncService;

    @GetMapping("/async")
    public void hello() {
        AsyncEntry asyncEntry = null;
        try {
            asyncEntry = SphU.asyncEntry("Sentinel_Async");
            asyncService.hello();
        } catch (BlockException e) {
            System.out.println("系统繁忙，请稍后");
        } finally {
            if (asyncEntry != null) {
                asyncEntry.exit();
            }
        }

    }

}
