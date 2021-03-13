package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao; // = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    @Transactional
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("账号或密码错误");
        }

        //程序能执行到这里，表示账号密码正确，我们只需判断其他内容

        //判断失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效，请联系管理员");
        }

        //判断ip地址是否可用
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址不可用，请联系管理员");
        }
        //判断账号是否锁定
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("该账号以锁定，请联系管理员");
        }

        return user;
    }

    @Override
    @Transactional
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }
}
