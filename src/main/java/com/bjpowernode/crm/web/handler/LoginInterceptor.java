package com.bjpowernode.crm.web.handler;

import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String path = request.getServletPath();
       // System.out.println(path);
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            return true;
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            System.out.println("============登录拦截器==========");
           // System.out.println(request.getContextPath() + "/login.jsp");
//            response.sendRedirect(request.getContextPath() + "/settings/user/login.do");
            request.getRequestDispatcher("/login.jsp").forward(request,response);
            return false;
        }
        return true;
    }
}
