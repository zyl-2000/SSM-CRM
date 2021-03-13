package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String Path = request.getServletPath();

        //不需要验证的资源，默认放行
        if ("/login.jsp".equals(Path) || "/settings/user/login.do".equals(Path)){
           filterChain.doFilter(servletRequest,servletResponse);
        }else {
            if (user != null){
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
            /*
                重定向的路径怎么写？
                 在实际项目开发中，对于路径的使用，不管前端还是后端，应该一律使用绝对路径
                 关于转发和重定向的路径写法如下：
                 转发：
                    使用的是一种特殊的绝对路径的使用方式，这种绝对路径前面不加/项目名，这种路径也称为内部路径
                    /login.jsp
                 重定向：
                    使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                    /crm/login.jsp

                  为什么使用重定向：
                      转发之后，路径会停留在老路径之上，而不是跳转之后的最新资源的路径
                      我们应该在为用户跳转到登录页的同时，将浏览器的地址栏应该自动设置为当前的登录页的路径
            */

                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }


    }

    @Override
    public void destroy() {

    }
}
