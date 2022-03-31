package com.tz.mybatisplus.config;

import com.tz.mybatisplus.interceptor.DecryptFieldInterceptor;
import com.tz.mybatisplus.interceptor.EncryptFieldInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author @醉鱼
 * @link https://github.com/TianPuJun
 * @ClassName EncryptionMapperConfig
 * @Description 注入拦截器
 * @Date 10:37 2022/3/30
 **/
@SpringBootConfiguration
public class EncryptionMapperConfig {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public String encryptionMapperConfig() {
        EncryptFieldInterceptor encryptFieldInterceptor = new EncryptFieldInterceptor();
        DecryptFieldInterceptor decryptFieldInterceptor = new DecryptFieldInterceptor();
        Properties properties = new Properties();
        properties.setProperty("username", "zuiyu");
        encryptFieldInterceptor.setProperties(properties);
        sqlSessionFactory.getConfiguration().addInterceptor(encryptFieldInterceptor);
        sqlSessionFactory.getConfiguration().addInterceptor(decryptFieldInterceptor);
        return "encryptionMapperConfig";
    }
}
