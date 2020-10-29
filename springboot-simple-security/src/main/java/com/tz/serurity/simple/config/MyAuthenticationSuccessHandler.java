package com.tz.serurity.simple.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author https://github.com/TianPuJun @醉鱼
 * @ClassName MyAuthenticationSucessHandler
 * @Description 登陆成功处理逻辑
 * @Date 11:47 2020/10/29
 **/
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * json 处理登陆成功逻辑
     */
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//        httpServletResponse.setContentType("application/json;charset=utf-8");
//        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
//    }

    /**
     * 成功页面跳转
     */
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        savedRequest.getRedirectUrl();
        redirectStrategy.sendRedirect(request, response, "/index");
    }
}
