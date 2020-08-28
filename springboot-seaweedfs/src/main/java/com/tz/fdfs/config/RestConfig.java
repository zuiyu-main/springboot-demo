package com.tz.fdfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author https://github.com/TianPuJun @256g的胃
 * @ClassName RestConfig
 * @Description
 * @Date 09:40 2020/8/22
 **/
@Configuration
public class RestConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
