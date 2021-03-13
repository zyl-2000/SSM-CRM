package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/*
    系统初始化监听器
 */
public class SysInitListener implements ServletContextListener {

    /*
        该方法是用来监听上下文(Application)域对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后，立马执行该方法

        参数：event
          该参数能够取得监听的对象
          例如我们监听的是上下文对象，通过该参数就可以取得上下文域对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器缓存数据字典开始");

        ServletContext application = event.getServletContext();

        DicService dicService = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()).getBean(DicServiceImpl.class);
        /*
              应该管业务层要7个list
              业务层应该要这样来保存数据：
                  map.put("appellationList",dvList1);
                  map.put("clueStateList",dvList2);
                  map.put("stageList",dvList3);
                  ...
                  ...
         */

        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> set = map.keySet();
        for (String key : set) {

            application.setAttribute(key,map.get(key));
        }
        System.out.println("处理数据字典结束");

        //------------------------------------------------------------------------

        /*
            数据字典处理完毕后,处理Stage2Possibility.properties文件

           处理 Stage2Possibility.properties文件步骤
              解析该文件,将该属性文件中的键值对关系处理成为java中键值对关系(map)

              Map<String(阶段stage),String(可能性possibility)> pMap = ...
              pMap.put("01...",10)
              pMap.put("02...",20)
              pMap.put("03...",30)
              pMap.put("07...",100)

              pMap保存后,放在服务器缓存中
              application.setAttribute("pMap",pMap)


         */

        //解析properties文件
        System.out.println("解析Stage2Possibility.properties文件");

        Map<String,String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = rb.getKeys();

        while (keys.hasMoreElements()){
            //阶段
            String key = keys.nextElement();
            //可能性
            String value = rb.getString(key);
            pMap.put(key,value);
        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);
        System.out.println("Stage2Possibility.properties文件解析完成");
    }
}
