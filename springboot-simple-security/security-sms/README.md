# spring security 集成短信验证码

> 只做备忘录,大体流程分析

* 配置访问登录页面404

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
    }
}
```
* 授权 验证成功响应,失败响应

```java
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        redirectStrategy.sendRedirect(request, response, "/index");
    }
}

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(mapper.writeValueAsString(exception.getMessage()));
    }
}
```

* springSecurity 安全配置,注入短信验证码的配置

```java
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;



    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginPage("/authentication/require")
                    .loginProcessingUrl("/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                .and()
                    .authorizeRequests()
                    .antMatchers("/authentication/require",
                            "/login.html", "/code/image","/code/sms","/css/**").permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .csrf().disable()
                .apply(smsAuthenticationConfig);
    }
}
```

* 自定义用户 服务 ,此处只返回了一个传入用户,密码为 123456 的账号,后续可以使用数据库,参考 [springboot-security](../../../../../../springboot-security/README.md)

```java
@Configuration
public class UserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户，替代数据库获取逻辑
        MyUser user = new MyUser();
        user.setUserName(username);
        user.setPassword(this.passwordEncoder.encode("123456"));
        // 输出加密后的密码
        System.out.println(user.getPassword());

        return new User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
```

* 短信验证码相关的配置,代码参考src/main/java/com/tz/sms/validate/smscode包下面的配置类
* 验证流程
  * UsernamePasswordAuthenticationFilter
  * AuthenticationManager
  * DaoAuthenticationProvider
  * UserDetailService
  * UserDetails
  * Authentication
  
Spring Security使用UsernamePasswordAuthenticationFilter过滤器来拦截用户名密码认证请求，将用户名和密码封装成一个UsernamePasswordToken对象交给AuthenticationManager处理。AuthenticationManager将挑出一个支持处理该类型Token的AuthenticationProvider（这里为DaoAuthenticationProvider，AuthenticationProvider的其中一个实现类）来进行认证，认证过程中DaoAuthenticationProvider将调用UserDetailService的loadUserByUsername方法来处理认证，如果认证通过（即UsernamePasswordToken中的用户名和密码相符）则返回一个UserDetails类型对象，并将认证信息保存到Session中，认证后我们便可以通过Authentication对象获取到认证的信息了。

* 仿照流程实现
  * SmsAuthentication
  * AuthenticationManager
  * SmsAuthenticationProvider
  * UserDetailsService
  * UserDetails
  * Authentication
  
在这个流程中，我们自定义了一个名为SmsAuthenticationFilter的过滤器来拦截短信验证码登录请求，并将手机号码封装到一个叫SmsAuthenticationToken的对象中。在Spring Security中，认证处理都需要通过AuthenticationManager来代理，所以这里我们依旧将SmsAuthenticationToken交由AuthenticationManager处理。接着我们需要定义一个支持处理SmsAuthenticationToken对象的SmsAuthenticationProvider，SmsAuthenticationProvider调用UserDetailService的loadUserByUsername方法来处理认证。与用户名密码认证不一样的是，这里是通过SmsAuthenticationToken中的手机号去数据库中查询是否有与之对应的用户，如果有，则将该用户信息封装到UserDetails对象中返回并将认证后的信息保存到Authentication对象中。

为了实现这个流程，我们需要定义SmsAuthenticationFilter、SmsAuthenticationToken和SmsAuthenticationProvider，并将这些组建组合起来添加到Spring Security中。

  
