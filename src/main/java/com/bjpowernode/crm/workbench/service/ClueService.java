package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import javax.servlet.RequestDispatcher;
import java.util.List;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    boolean deleteDetail(String id);

    boolean saveRelation(String[] aIds, String cId);


    boolean convert(Tran t, String clueId, String createBy);
}
