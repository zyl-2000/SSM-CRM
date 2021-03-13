package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    ArrayList<Activity> getActivityListByCondition(Map<String, Object> map);

    int deleteByIds(String[] ids);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByNameAndIdNotInCAR(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
