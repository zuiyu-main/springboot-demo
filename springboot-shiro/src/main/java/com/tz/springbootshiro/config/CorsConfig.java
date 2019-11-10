package com.tz.springbootshiro.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author tz
 * @Classname CorsConfig
 * @Description
 * @Date 2019-11-10 10:56
 */
@SpringBootConfiguration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1
        corsConfiguration.addAllowedOrigin("*");
        //2
        corsConfiguration.addAllowedHeader("*");
        //3
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //4
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
