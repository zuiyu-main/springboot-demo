package com.zuiyu.sentinel.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName AsyncService
 * @Description
 * @Date 15:08 2022/6/12
 **/
@Service
public class AsyncService {
    @Async
    public void hello() {
        System.out.println("异步开始======");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步结束======");
    }
}
