package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    //线索相关表
    @Resource
    private ClueDao clueDao;// = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;// = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    @Resource
    private ClueRemarkDao clueRemarkDao;// = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    @Resource
    private CustomerDao customerDao;// = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Resource
    private CustomerRemarkDao customerRemarkDao;// = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    @Resource
    private ContactsDao contactsDao;// = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    @Resource
    private ContactsRemarkDao contactsRemarkDao;// = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;// = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    @Resource
    private TranDao tranDao;// = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    @Resource
    private TranHistoryDao tranHistoryDao;// = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    @Transactional
    public boolean save(Clue clue) {
        int count = clueDao.save(clue);
        if (count == 0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Clue detail(String id) {
        Clue clue = clueDao.queryClueById(id);
        return clue;
    }

    @Override
    @Transactional
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList = clueDao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    @Transactional
    public boolean deleteDetail(String id) {
        boolean flag = true;
        int count = clueDao.deleteDetail(id);
        if (count != 1){
            flag =false;
        }
        return flag;
    }

    @Override
    @Transactional
    public boolean saveRelation(String[] aIds, String cId) {
        boolean flag = true;
        for (String aId : aIds) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cId);
            car.setActivityId(aId);
            int count = clueActivityRelationDao.saveRelation(car);
            if (count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    @Transactional
    public boolean convert(Tran t, String clueId, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        //(1)通过线索id获取线索对象(线索对象当中封装了线索的信息)
        Clue clue = clueDao.queryClueById(clueId);
        //(2)通过线索对象提取客户信息,当客户不存在的时候,新建客户(根据公司名称精确匹配,判断该客户是否存在)
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);

        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());

            //添加客户
            int count1 = customerDao.save(customer);
            if (count1!=1){
                flag=false;
            }
        }

        //(3)通过线索对象提取联系人信息,保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        //添加联系人
        int count2 = contactsDao.save(contacts);
        if (count2!=1){
            flag=false;
        }

        //(4)线索备注转换到客户备注以及联系人备注
        //查询出与该线索关联的备注信息表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {

            //取出备注信息,主要需要转换的就是这个备注信息
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象,添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");

            int count3 = customerRemarkDao.save(customerRemark);
            if (count3!=1){
                flag=false;
            }

            //创建联系人备注对象,添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4!=1){
                flag=false;
            }

        }

        //(5)"线索和市场活动"的关联关系转换到"联系人和市场活动"的关系
        //查询出与该条线索关联的市场活动,查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            String activityId = clueActivityRelation.getActivityId();
            //创建联系人市场活动关系表对象
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1){
                flag=false;
            }
        }

        //(6)如果有创建交易的需求,需要创建交易
        if (t!=null){
            /*
               t对象在controller里面已经封装好的对象如下
                 id,money,name,expectedDate,stage,activityId,createBy,createTime

               接下来可以通过Clue对象,取出一些信息,继续完善对象Tran的封装

             */
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setCustomerId(customer.getId());
            t.setContactSummary(customer.getContactSummary());
            t.setContactsId(contacts.getId());
            //添加交易
            int count6 = tranDao.save(t);
            if (count6!=1){
                flag=false;
            }

            //(7)如果创建了交易,则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate((t.getExpectedDate()));
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());

            int count7 = tranHistoryDao.save(tranHistory);
            if (count7!=1){
                flag=false;
            }
        }

        //(8)删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
            int count8 = clueRemarkDao.delete(clueRemark);
            if (count8!=1){
                flag=false;
            }
        }

        //(9)删除线索和市场活动的关联关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if (count9!=1){
                flag=false;
            }
        }

        //(10)删除线索
       /* int count10 = clueDao.delete(clueId);
        if (count10 != 1){
            flag=false;
        }*/
        return flag;
    }

}
