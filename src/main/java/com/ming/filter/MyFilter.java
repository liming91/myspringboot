package com.ming.filter;

import org.apache.catalina.servlet4preview.http.HttpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import javax.servlet.*;
import java.io.IOException;

/**
 * 自定义filter要注册servlet要用springboot提供的FilterRegistrationBean
 */
public class MyFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //过滤请求
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter Process...");
        //放行请求
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
