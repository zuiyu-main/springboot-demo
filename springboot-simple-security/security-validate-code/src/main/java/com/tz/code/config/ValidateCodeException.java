package com.tz.code.config;

import org.springframework.security.core.AuthenticationException;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName ValidateCodeException
 * @Description 验证码校验流程
 * @Date 14:28 2020/10/29
 **/
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String message) {
        super(message);
    }
}
