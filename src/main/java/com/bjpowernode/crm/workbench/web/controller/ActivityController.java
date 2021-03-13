package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.util.Set;

@Controller
@RequestMapping("/workbench")
public class ActivityController extends HttpServlet {
/*    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path = req.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(req,resp);

        }else if ("/workbench/activity/save.do".equals(path)){
            save(req,resp);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(req,resp);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(req,resp);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(req,resp);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(req,resp);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(req,resp);
        }else if ("/workbench/activity/editRemark.do".equals(path)){
            editRemark(req,resp);
        }
    }*/

    @Resource
    private ActivityService as;

    @Resource
    private UserService us;

    @RequestMapping("/activity/editRemark.do")
    @ResponseBody
    private Map<String,Object> editRemark(ActivityRemark ar,HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("修改备注信息");

        //修改时间使用系统当前时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人使用当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        boolean flag = as.editRemark(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("ar",ar);
        map.put("success",flag);
        return map;

    }

    @RequestMapping("/activity/saveRemark.do")
    @ResponseBody
    private void saveRemark(ActivityRemark ar,HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("保存备注信息");

        String id = UUIDUtil.getUUID();
        String editFlag = "0";
        //创建时间使用系统当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人使用当前登录用户
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        ar.setId(id);
        ar.setEditFlag(editFlag);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);

        boolean flag = as.saveRemark(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("ar",ar);
        map.put("success",flag);
        PrintJson.printJsonObj(resp,map);

    }

    @RequestMapping("/activity/deleteRemark.do")
    @ResponseBody
    private boolean deleteRemark(String id) {
        System.out.println("删除备注的操作");
        boolean flag = as.deleteById(id);
        return flag;
    }

    @RequestMapping("/activity/getRemarkListByAid.do")
    @ResponseBody
    private List<ActivityRemark> getRemarkListByAid(String aId) {
        System.out.println("通过aId获取市场活动备注列表");
        List<ActivityRemark> list = as.getRemarkListByAId(aId);
        return list;
    }

    @RequestMapping("/activity/detail.do")
    private ModelAndView detail(String id) {

        Activity a = as.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("a",a);
        mv.setViewName("/workbench/activity/detail.jsp");
//        req.setAttribute("a",a);
//        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
        return mv;
    }


    @RequestMapping("/activity/update.do")
    @ResponseBody
    private boolean update(Activity activity,HttpServletRequest req) {
        System.out.println("进入到修改操作");

        //修改时间使用系统当前时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人使用当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        boolean flag = as.update(activity);
        return flag;
    }

    @RequestMapping("/activity/getUserListAndActivity.do")
    @ResponseBody
    private Map<String,Object> getUserListAndActivity(String id) {
        System.out.println("修改市场活动，得到用户列表和通过id得到市场活动单条");
        Map<String,Object> map = as.getUserListAndActivity(id);
        return map;
    }

    @RequestMapping("/activity/delete.do")
    @ResponseBody
    private boolean delete(String[] ids) {
//        String[] ids = req.getParameterValues("id");

        boolean flag = as.delete(ids);
        return flag;
    }

    @RequestMapping("/activity/pageList.do")
    @ResponseBody
    public PaginationVO pageList(@RequestParam Map<String,Object> map) {
       // Integer skipNo = (pageNo-1)*pageSize;

/*        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("skipNo",skipNo);
        map.put("pageSize",pageSize);
        map.put("owner",owner);
        map.put("startDate",startDate);
*/



        int i = Integer.parseInt((String)map.get("pageNo"))-1;
        int j = Integer.parseInt((String)map.get("pageSize")) ;
        map.put("skipNo",i*j);
        map.put("pageSize",j);

        /*
            前端需要的返回值：
                 1、市场活动信息列表
                 2、查询到总条数

                 业务层拿到了以上两项信息后做返回
                 map

                 vo ：这个功能使用比较频繁，之后模块也可以使用，选用vo
                 在类上加泛型：不同的业务也可使用
                 PaginationVO<T>
                     private int total;
                     private List<T> dataList;
        */

        PaginationVO pageVO = as.pageList(map);

        //PrintJson.printJsonObj(resp,pageVO);
        return pageVO;
    }

    @RequestMapping("/activity/save.do")
    @ResponseBody
    private boolean save(Activity activity,HttpServletRequest req) {
        System.out.println("进入到保存操作");
        String id = UUIDUtil.getUUID();
     /*   String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");*/

        //创建时间使用系统当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人使用当前登录用户
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        boolean flag = as.save(activity);
        System.out.println("========================="+flag);
        return flag;
    }

    @RequestMapping("/activity/getUserList.do")
    @ResponseBody
    private List<User> getUserList() {
        System.out.println("取得用户信息列表");

        List<User> userList = us.getUserList();

        return userList;
    }
}
