package com.bjpowernode.crm.settings.web.controller;


import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings")
public class UserController{
/*
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(req,resp);

        }else if ("/settings/user/xxx.do".equals(path)){

        }
    }
*/
    @Resource
    private UserService us;

    @RequestMapping("/user/login.do")
    @ResponseBody
    public Map<String,Object> login(String loginAct,String loginPwd, HttpServletRequest req) {
//        String loginAct = req.getParameter("loginAct");
//        String loginPwd = req.getParameter("loginPwd");
//        System.out.println(loginAct);
        String ip = req.getRemoteAddr();

        //把密码的明文以md5的形式加密，可以与数据库对比
        loginPwd = MD5Util.getMD5(loginPwd);
        // = (UserService) ServiceFactory.getService(new UserServiceImpl());
        Map<String,Object> map = new HashMap<>();
        try {
            User user = us.login(loginAct,loginPwd,ip);
            req.getSession().setAttribute("user",user);
            //程序执行到此处，表示业务层没有为controller抛出任何异常
            //表示登录成功
            /*
               {"success":true}
            */
            map.put("success",true);
           // PrintJson.printJsonFlag(resp,true);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            //对于请求失败的处理
            /*
              可以有两种方法来处理：
                 1.把消息结果都放到map里
                 2.封装一个vo对象
                   private boolean success;
                   private String msg;

               如果对于展现的信息将来还会大量使用，我们可以封装为vo类
               如果展现的信息只有当前的需求有用，只需放在map里
            */
            String msg = e.getMessage();
            System.out.println("========"+msg +"========");

            map.put("success",false);
            map.put("msg",msg);
           // PrintJson.printJsonObj(resp,map);
            return map;
        }

    }

}
