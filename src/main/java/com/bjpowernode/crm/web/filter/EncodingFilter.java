package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //过滤post请求中文参数乱码问题
        servletRequest.setCharacterEncoding("UTF-8");

        //过滤响应流的字符编码乱码问题
        servletResponse.setContentType("text/html;charset=UTF-8");

        //将请求放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
