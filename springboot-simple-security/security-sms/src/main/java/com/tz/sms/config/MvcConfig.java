package com.tz.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tz
 * @Classname SecurityConfig
 * @Description 如果不加此配置类, 访问登录页面 404,需要在 resources 目录下面新建一个 resources/resources/css/login.css与resources/resources/login.html
 * @Date 2019-12-08 10:06
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
    }
}
