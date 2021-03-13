package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO pageList(Map<String,Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAId(String aId);

    boolean deleteById(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean editRemark(ActivityRemark ar);

    List<Activity> getActivityListByNameAndIdNotInCAR(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
