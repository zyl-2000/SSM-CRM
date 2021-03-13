package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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


@RequestMapping("workbench/transaction")
@Controller
public class TranController extends HttpServlet {
/*    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入线索模块控制器");
        String path = req.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)) {
            add(req,resp);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(req,resp);
        } else if ("/workbench/transaction/save.do".equals(path)) {
            save(req,resp);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(req,resp);
        } else if ("/workbench/transaction/historyListByTranId.do".equals(path)) {
            historyListByTranId(req,resp);
        } else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(req,resp);
        } else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(req,resp);
        }
    }*/

    @Resource
    private UserService us;
    @Resource
    private TranService tranService;
    @Resource
    private CustomerService customerService;


    @RequestMapping("/getCharts.do")
    @ResponseBody
    private Map<String,Object> getCharts() {
        Map<String,Object> map = tranService.getCharts();
        return map;
    }

    @RequestMapping("/changeStage.do")
    @ResponseBody
    private Map<String,Object> changeStage(Tran tran,HttpServletRequest req, HttpServletResponse resp) {

        //创建时间使用系统当前时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人使用当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        Map<String,String> pMap = (Map<String, String>) req.getServletContext().getAttribute("pMap");

        tran.setEditTime(editTime);
        tran.setEditBy(editBy);
        tran.setPossibility(pMap.get(tran.getStage()));

        boolean success = tranService.changeStage(tran);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        map.put("tran",tran);
        return map;
    }


    @RequestMapping("/historyListByTranId.do")
    @ResponseBody
    private List<TranHistory> historyListByTranId(String tranId,HttpServletRequest req) {
        List<TranHistory> hList = tranService.historyListByTranId(tranId);
        for (TranHistory tranHistory : hList) {
            String stage = tranHistory.getStage();
            Map<String,String> pMap = (Map<String, String>) req.getServletContext().getAttribute("pMap");
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        return hList;
    }


    @RequestMapping("/detail.do")
    private ModelAndView detail(String id,HttpServletRequest req) {
        System.out.println("跳转到交易详细信息页");
        Tran tran = tranService.detail(id);

        String stage = tran.getStage();
        Map<String,String> map = (Map<String, String>) req.getServletContext().getAttribute("pMap");
        String possibility = map.get(stage);
        tran.setPossibility(possibility);
        ModelAndView mv = new ModelAndView();
        mv.addObject("tran",tran);
        mv.setViewName("/workbench/transaction/detail.jsp");
        return mv;
    }


    @RequestMapping("/save.do")
    private ModelAndView save(Tran tran,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("保存交易");
        String id = UUIDUtil.getUUID();
        String customerName = req.getParameter("customerName");
        //创建时间使用系统当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人使用当前登录用户
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        tran.setId(id);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);

        // = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = tranService.save(tran,customerName);

        ModelAndView mv = new ModelAndView();
        if (flag){
            mv.setViewName("redirect:"+ "/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    private List<String> getCustomerName(String name) {
        System.out.println("取得客户名称列表(按照客户名称模糊查询)");
        List<String> sList = customerService.getCustomerName(name);
        return sList;
    }


    @RequestMapping("/add.do")
    private ModelAndView add(){
        // = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        ModelAndView mv = new ModelAndView();
        mv.addObject("uList",userList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;
    }
}
