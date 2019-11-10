package com.tz.springbootshiro.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Component;

/**
 * @author tz
 * @Classname SimpleService
 * @Description 测试接口访问权限，可读可写
 * @Date 2019-11-09 11:50
 */
@Slf4j
@Component
public class SimpleService {

    @RequiresPermissions("write")
    public void writeRestrictedCall() {
        log.info("executing method that requires the 'write' permission");
    }

    @RequiresPermissions("read")
    public void readRestrictedCall() {
        log.info("executing method that requires the 'read' permission");
    }
}
