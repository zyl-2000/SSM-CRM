package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;

import javax.servlet.RequestDispatcher;
import java.util.List;

public interface ClueDao {


    int save(Clue clue);

    Clue queryClueById(String id);

    List<Activity> getActivityListByClueId(String clueId);

    int deleteDetail(String id);

    int delete(String clueId);
}
