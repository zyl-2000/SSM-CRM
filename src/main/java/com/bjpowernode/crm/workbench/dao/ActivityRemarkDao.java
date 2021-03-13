package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int queryByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAId(String aId);

    int deleteById(String id);

    int saveRemark(ActivityRemark ar);

    int editRemark(ActivityRemark ar);
}
