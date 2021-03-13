package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao ad;// = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    @Resource
    private ActivityRemarkDao ard;// = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    @Resource
    private UserDao userDao; // = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    @Transactional
    public boolean save(Activity activity) {
        System.out.println("保存操作的service");
        boolean flag = true;

        int count = ad.save(activity);
        //为防止在执行save操作时出现异常，此处判断不执行
        //因此此处还应该抛出一个自定义异常

        if (count != 1){
            flag = false;
        }
        System.out.println(flag+"===================================");
        return flag;
    }

    @Override
    @Transactional
    public PaginationVO pageList(Map<String, Object> map) {
        PaginationVO pageVO = new PaginationVO();

        ArrayList<Activity> dataList = ad.getActivityListByCondition(map);
        int count = ad.getTotalByCondition(map);

        pageVO.setDataList(dataList);
        pageVO.setTotal(count);
        return pageVO;
    }

    @Override
    @Transactional
    public boolean delete(String[] ids) {
        System.out.println("进入到删除操作");
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = ard.queryByAids(ids);
        //删除备注
        int count2 = ard.deleteByAids(ids);

        if (count1 != count2){
            flag = false;
        }
        //删除市场活动
        int count3 = ad.deleteByIds(ids);
        if (count3 != ids.length){
            flag =false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> uList = userDao.getUserList();
        Activity a = ad.getById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);
        return map;
    }

    @Override
    @Transactional
    public boolean update(Activity activity) {
        System.out.println("修改操作的service");
        boolean flag = true;

        int count = ad.update(activity);
        //为防止在执行save操作时出现异常，此处判断不执行
        //因此此处还应该抛出一个自定义异常

        if (count != 1){
            flag = false;
        }

        return flag;

    }

    @Override
    @Transactional
    public Activity detail(String id) {
        Activity a = ad.detail(id);
        return a;
    }

    @Override
    @Transactional
    public List<ActivityRemark> getRemarkListByAId(String aId) {
        List<ActivityRemark> list = ard.getRemarkListByAId(aId);
        return list;
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        boolean flag = true;
        int count = ard.deleteById(id);
        if (count == 0){
            flag = false;
        }
        return flag;
    }

    @Override
    @Transactional
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = ard.saveRemark(ar);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    @Transactional
    public boolean editRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = ard.editRemark(ar);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    @Transactional
    public List<Activity> getActivityListByNameAndIdNotInCAR(Map<String, String> map) {
        List<Activity> list = ad.getActivityListByNameAndIdNotInCAR(map);
        return list;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> list = ad.getActivityListByName(aname);
        return list;
    }
}
