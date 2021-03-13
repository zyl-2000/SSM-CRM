package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;// = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    @Resource
    private TranHistoryDao tranHistoryDao;// = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    @Resource
    private CustomerDao customerDao;// = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    @Transactional
    public boolean save(Tran tran, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setContactSummary(tran.getContactSummary());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setDescription(tran.getDescription());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            int count1 = customerDao.save(customer);
            if (count1!=1){
                flag=false;
            }
        }

        tran.setCustomerId(customer.getId());
        int count2 = tranDao.save(tran);
        if (count2!=1){
            flag=false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        int count3 = tranHistoryDao.save(tranHistory);
        if (count3!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    @Transactional
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    @Transactional
    public List<TranHistory> historyListByTranId(String tranId) {
        List<TranHistory> hList = tranHistoryDao.historyListByTranId(tranId);
        return hList;
    }

    @Override
    @Transactional
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count1 = tranDao.changeStage(tran);
        if (count1!=1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setCreateBy(tran.getEditBy());
        th.setCreateTime(tran.getEditTime());
        th.setExpectedDate(tran.getExpectedDate());
        th.setMoney(tran.getMoney());
        th.setStage(tran.getStage());
        th.setTranId(tran.getId());
        th.setId(UUIDUtil.getUUID());
        int count2 = tranHistoryDao.save(th);
        if (count2!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    @Transactional
    public Map<String, Object> getCharts() {
        //获取总数量
        int total = tranDao.getTotal();

        //按阶段获取数量
        List<Map<String,String>> dataList = tranDao.getCountByStage();

        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
