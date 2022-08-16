package com.zuiyu.client.config;

import com.zuiyu.client.interceptor.LicenseCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author zuiyu
 * @date 2022/8/15
 * @description
 * @link https://github.com/zuiyu-main
 */
@Configuration
@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private LicenseCheckInterceptor licenseCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //添加要拦截的url
        registry.addInterceptor(licenseCheckInterceptor)
                // 拦截的路径
                .addPathPatterns("/**");
        // 放行的路径
//                .excludePathPatterns("/admin/login");
    }
}
