package com.tz.serurity.simple.config;

import com.tz.serurity.simple.controller.ValidateController;
import com.tz.serurity.simple.exceptiion.ValidateCodeException;
import com.tz.serurity.simple.model.ImageCode;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName ValidateCodeFilter
 * @Description 验证码校验filter
 * @Date 14:29 2020/10/29
 **/
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * Spring Security实际上是由许多过滤器组成的过滤器链，
     * 处理用户登录逻辑的过滤器为UsernamePasswordAuthenticationFilter，
     * 而验证码校验过程应该是在这个过滤器之前的，即只有验证码校验通过后采去校验用户名和密码。
     * 由于Spring Security并没有直接提供验证码校验相关的过滤器接口，
     * 所以我们需要自己定义一个验证码校验的过滤器ValidateCodeFilter：
     * 在doFilterInternal方法中我们判断了请求URL是否为/login，
     * 该路径对应登录form表单的action路径，请求的方法是否为POST，是的话进行验证码校验逻辑，
     * 否则直接执行filterChain.doFilter让代码往下走。
     * 当在验证码校验的过程中捕获到异常时，调用Spring Security的校验失败处理器AuthenticationFailureHandler进行处理。
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (StringUtils.equalsIgnoreCase("/login", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validateCode(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 我们分别从Session中获取了ImageCode对象和请求参数imageCode（对应登录页面的验证码<input>框name属性）,
     * 然后进行了各种判断并抛出相应的异常。
     * 当验证码过期或者验证码校验通过时，我们便可以删除Session中的ImageCode属性了。
     *
     * @param servletWebRequest
     * @throws ServletRequestBindingException
     */
    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (codeInSession.isExpire()) {
            sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);

    }
}
