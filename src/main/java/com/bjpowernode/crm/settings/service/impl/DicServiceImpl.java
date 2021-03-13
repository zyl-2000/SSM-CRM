package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {
    @Resource
    private DicTypeDao dicTypeDao; // = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    @Resource
    private DicValueDao dicValueDao; // = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();

        //获取所有的DicType
        List<DicType> typeList = dicTypeDao.getAllType();

        for (DicType type : typeList) {
            //通过TypeCode取得DicValue
            List<DicValue> valueList = dicValueDao.getValueByCode(type.getCode());
            map.put(type.getCode(),valueList);
        }

        return map;
    }
}
