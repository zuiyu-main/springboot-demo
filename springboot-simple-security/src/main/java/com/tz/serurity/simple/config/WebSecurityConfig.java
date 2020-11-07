package com.tz.serurity.simple.config;

import com.tz.serurity.simple.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author tz
 * @Classname WebSecurityConfig
 * @Description
 * @Date 2019-12-18 10:51
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private DataSource dataSource;
    @Resource
    private MyUserDetailService userDetailService;

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


        http
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                // remember 过期时间，单为秒
                .tokenValiditySeconds(3600)
                // 处理自动登录逻辑
                .userDetailsService(userDetailService)
                .and()
                // 添加验证码拦截器在 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(validateCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                // 表单登录
                .formLogin()
                // HTTP Basic
//                . http.httpBasic()
                // 登录跳转 URL
                .loginPage("/authentication/require")
                // 处理表单登录 URL
                .loginProcessingUrl("/login")
                // 处理登录成功
                .successHandler(myAuthenticationSuccessHandler())
                // 处理登录失败
                .failureHandler(myAuthenticationFailureHandler())
                .and()

                // 授权配置
                .authorizeRequests()
                // 登录跳转 URL 无需认证
                .antMatchers("/authentication/require",
                        "/login.html",
                        "/code/image").permitAll()
                .antMatchers("/r/r1").hasAuthority("p1")
                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/r/**").authenticated()
                // 所有请求
                .anyRequest().permitAll()
                // 都需要认证
//                .authenticated()
                .and().csrf().disable();
    }

    /**
     * 密码验证方式
     * NoOpPasswordEncoder.getInstance() 字符串校验
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证成功处理
     *
     * @return
     */
    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler();
    }

    /**
     * 认证失败处理
     *
     * @return
     */
    @Bean
    public MyAuthenticationFailureHandler myAuthenticationFailureHandler() {
        return new MyAuthenticationFailureHandler();
    }

    /**
     * 验证码过滤器
     *
     * @return
     */
    @Bean
    public ValidateCodeFilter validateCodeFilter() {
        return new ValidateCodeFilter();
    }

    /**
     * 记住我持久化使用
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }
}
