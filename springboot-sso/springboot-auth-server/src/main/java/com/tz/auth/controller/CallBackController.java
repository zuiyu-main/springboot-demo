package com.tz.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName CallBackController
 * @Description 回调
 * @Date 18:18 2020/12/7
 **/
@RestController
public class CallBackController {
    @GetMapping("/")
    public Object index(Authentication authentication) {
        return authentication;
    }
}
