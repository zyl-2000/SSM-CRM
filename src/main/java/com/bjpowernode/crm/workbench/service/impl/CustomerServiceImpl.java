package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;// = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    @Transactional
    public List<String> getCustomerName(String name) {
        List<String> sList = customerDao.getCustomerName(name);
        return sList;
    }
}
