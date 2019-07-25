package com.ranger.config.http;

import com.ranger.utils.RedisStringUtil;
import com.ranger.utils.RedisUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class UserRequestFilter implements Filter {
//    private UserTokenRepository userTokenRepository;

    private RedisStringUtil redisUtil;

    public UserRequestFilter(RedisStringUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UserRequestWrapper userRequestWrapper = new UserRequestWrapper((HttpServletRequest) servletRequest, redisUtil);
        filterChain.doFilter(userRequestWrapper, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
