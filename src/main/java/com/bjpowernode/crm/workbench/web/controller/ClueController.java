package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController extends HttpServlet {
    /*@Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索活动控制器");
        String path = req.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(req,resp);
        } else if ("/workbench/clue/save.do".equals(path)) {
            save(req,resp);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(req,resp);
        }else if ("/workbench/clue/getActivityByClueId.do".equals(path)) {
            getActivityByClueId(req,resp);
        }else if ("/workbench/clue/deleteDetail.do".equals(path)) {
            deleteDetail(req,resp);
        }else if ("/workbench/clue/getActivityListByNameAndIdNotInCAR.do".equals(path)) {
            getActivityListByNameAndIdNotInCAR(req,resp);
        }else if ("/workbench/clue/saveRelation.do".equals(path)) {
            saveRelation(req,resp);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(req,resp);
        }else if ("/workbench/clue/convert.do".equals(path)) {
            convert(req,resp);
        }
    }*/

    @Resource
    private UserService us;
    @Resource
    private ActivityService activityService;
    @Resource
    private ClueService cs;


    @RequestMapping("/convert.do")
    private ModelAndView convert(String clueId,String flag,Tran t,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入线索转换操作");
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        //将交易t,传递到业务层,如果业务层里仍然为空这不需创建交易
        //如果把flag传递到业务层进行判断的话,需要同时传递Tran,共俩个参数,因此只传递一个Tran即可

        //如果需要创建交易
        if ("a".equals(flag)){

            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }else {
            t = null;
        }
        System.out.println("=========================" + flag + "=================="  +t);
        boolean flag1 = cs.convert(t,clueId,createBy);
        ModelAndView mv = new ModelAndView();

        if (flag1){
            mv.setViewName("redirect:" +"/workbench/clue/index.jsp");
        }
        return mv;
    }


    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    private List<Activity> getActivityListByName(String aname) {
        List<Activity> list = activityService.getActivityListByName(aname);
        return list;
    }

    @RequestMapping("/saveRelation.do")
    @ResponseBody
    private boolean saveRelation(String[] aIds,String cId) {

        boolean flag = cs.saveRelation(aIds,cId);
        return flag;
    }


    @RequestMapping("/getActivityListByNameAndIdNotInCAR.do")
    @ResponseBody
    private List<Activity> getActivityListByNameAndIdNotInCAR(String name,String cId) {

        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("cId",cId);
        List<Activity> aList = activityService.getActivityListByNameAndIdNotInCAR(map);
        return aList;
    }


    @RequestMapping("/deleteDetail.do")
    @ResponseBody
    private boolean deleteDetail(String id) {
        boolean success = cs.deleteDetail(id);
        return success;
    }

    @RequestMapping("/getActivityByClueId.do")
    @ResponseBody
    private List<Activity> getActivityByClueId(String clueId) {

        List<Activity> aList = cs.getActivityListByClueId(clueId);
        return aList;
    }

    @RequestMapping("/detail.do")
    private ModelAndView detail(String id) {

        Clue clue = cs.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    private boolean save(Clue clue,HttpServletRequest req) {
        String id = UUIDUtil.getUUID();
        //创建时间使用系统当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人使用当前登录用户
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);

        boolean success = cs.save(clue);
        return success;
    }


    @RequestMapping("/getUserList.do")
    @ResponseBody
    protected List<User> getUserList() {
        System.out.println("取得用户信息列表");
        // = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();

        return userList;
    }
}
