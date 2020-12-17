package com.tz.code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName BrowserSecurityConfig
 * @Description
 * @Date 15:51 2020/11/23
 **/
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSucessHandler;
    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private MyLogOutSuccessHandler myLogOutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     *
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSucessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                // 授权配置,所有请求,都需要验证
                .authorizeRequests()
                .antMatchers("/authentication/require",
                        "/login.html", "/code/sms", "/css/**", "/signout", "/signout/success",
                        "/code/image").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/signout")
                // 处理成功响应 url 和 handler 保留一个
                .logoutSuccessUrl("/signout/success")
//                .logoutSuccessHandler(myLogOutSuccessHandler)
                .deleteCookies("JESSIONID")
                .and().csrf().disable();
    }


}
