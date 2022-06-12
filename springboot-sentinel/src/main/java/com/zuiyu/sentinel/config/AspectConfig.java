package com.zuiyu.sentinel.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName AspectConfig
 * @Description
 * @Date 15:19 2022/6/12
 **/
@Configuration
public class AspectConfig {
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
