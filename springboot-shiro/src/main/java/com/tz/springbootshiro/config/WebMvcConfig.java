package com.tz.springbootshiro.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author tz
 * @Classname WebMvcConfig
 * @Description
 * @Date 2019-11-10 10:12
 */
@SpringBootConfiguration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/templates/**.js").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/templates/**.css").addResourceLocations("classpath:/templates/");
//其他静态资源
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
