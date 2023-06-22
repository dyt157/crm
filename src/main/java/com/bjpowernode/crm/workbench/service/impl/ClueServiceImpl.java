package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.pojo.*;
import com.bjpowernode.crm.workbench.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Program:ClueServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/16
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueMapper clueMapper;
    @Resource
    private ClueRemarkService clueRemarkService;
    @Resource
    private ClueActivityRelationService clueActivityRelationService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private ContactsRemarkService contactsRemarkService;
    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerRemarkService customerRemarkService;
    @Resource
    private ContactsActivityRelationService contactsActivityRelationService;
    @Resource
    private TranService tranService;
    @Resource
    private TranRemarkService tranRemarkService;
    @RequestMapping("/toIndex")
    @Override
    public int saveClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public PageInfo<Clue> queryClueForPage(Integer pageNum, Integer pageSize, Integer navigatePages) {
        PageHelper.startPage(pageNum,pageSize);
        List<Clue> clues = clueMapper.selectAllClue();
        PageInfo<Clue> cluePageInfo = new PageInfo<>(clues,navigatePages);
        return cluePageInfo;
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int modifyClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public PageInfo<Clue> queryClueByCondition(Integer pageNum,Integer pageSize,Clue clue) {
        PageHelper.startPage(pageNum,pageSize);
        List<Clue> clues = clueMapper.selectClueByCondition(clue);
        System.out.println("------------------");
        System.out.println(clues);
        PageInfo<Clue> cluePageInfo = new PageInfo<>(clues, 5);
        return cluePageInfo;
    }

    @Override
    public Clue queryClueByIdToDetail(String id) {
        return clueMapper.selectAClueByIdToDetail(id);
    }

    @Override
    @Transactional
    public void clueConvert(String clueId, User user, Tran tran, boolean isCreateTran) {
        //根据线索id查询出该线索的全部原始信息
        Clue clue = queryClueById(clueId);
        //线索中的信息转化到客户表中
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getId());
        //创建人和创建时间（当前时间点下）
        String dateTime = DateUtils.formatDateTime(new Date());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(dateTime);
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setWebsite(clue.getWebsite());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getCompany());
        customer.setDescription(clue.getDescription());
        customer.setPhone(clue.getPhone());

        //线索中的信息转换到联系人中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getId());
        contacts.setCustomerId(customer.getId());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(dateTime);

        //调用Service层，插入数据
        customerService.saveCustomer(customer);
        contactsService.saveContacts(contacts);

        //线索备注信息转换到客户备注表和联系人备注表中
        //这条线索必须有备注信息才进行转换
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkByClueIdForConvert(clueId);
        if (clueRemarks!=null&&clueRemarks.size()>0){//这一步不要忘了，很重要
            ArrayList<ContactsRemark> contactsRemarkList = new ArrayList<>();
            ArrayList<CustomerRemark> customerRemarkList = new ArrayList<>();
            for (ClueRemark clueRemark:clueRemarks){
                //联系人备注对象
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setId(UUIDUtils.getId());
                //客户备注对象
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setId(UUIDUtils.getId());

                //添加到集合中
                contactsRemarkList.add(contactsRemark);
                customerRemarkList.add(customerRemark);
            }
            contactsRemarkService.saveContactsRemarkList(contactsRemarkList);
            customerRemarkService.saveCustomerRemarkList(customerRemarkList);
        }
        //把线索和市场活动的关联关系转换到联系人和市场活动的关联关系中
        //这条线索必须有关联的市场活动才进行转换，不然会报错
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationService.queryClueActivityRelationListByClueId(clueId);
        if (clueActivityRelationList!=null&&clueActivityRelationList.size()>0){
            ArrayList<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtils.getId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                //添加到集合中
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            contactsActivityRelationService.saveContactsActivityRelationList(contactsActivityRelationList);
        }


        //创建交易
        if (isCreateTran){//用户在前段点击了创建交易的复选框，
            //我们在转换线索页面进行交易的创建，是一种快速创建方式
            //所以有些数据前端没有发送过来，我们就不用给这些属性赋值，空着就行
            tran.setId(UUIDUtils.getId());
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(dateTime);
            tran.setCustomerId(customer.getId());
            tran.setOwner(user.getId());//所有者前端也没有发送过来，但所有者又不能空着，我们就把创建者当成所有者得了
            //tran.setDescription();
            //tran.setContactSummary();
            //tran.setSource();
            //tran.setNextContactTime();
            //tran.setType();
            tranService.saveTran(tran);

            //线索的备注转换到交易备注中
            ArrayList<TranRemark> tranRemarkList = new ArrayList<>();
            if (clueRemarks!=null&&clueRemarks.size()>0){
                for (ClueRemark clueRemark:clueRemarks) {
                    TranRemark tranRemark = new TranRemark();
                    tranRemark.setId(UUIDUtils.getId());
                    tranRemark.setCreateBy(clueRemark.getCreateBy());
                    tranRemark.setCreateTime(clueRemark.getCreateTime());
                    tranRemark.setEditBy(clueRemark.getEditBy());
                    tranRemark.setEditTime(clueRemark.getEditTime());
                    tranRemark.setEditFlag(clueRemark.getEditFlag());
                    tranRemark.setNoteContent(clueRemark.getNoteContent());
                    tranRemark.setTranId(tran.getId());
                    tranRemarkList.add(tranRemark);
                }
            }
            tranRemarkService.saveTranRemarkList(tranRemarkList);
        }
        //删除线索备注
        clueRemarkService.deleteClueRemarkByClueId(clueId);
        //删除线索和市场活动之间的关联
        clueActivityRelationService.releaseContact(clueId,null);
        //删除线索
        String[] ids = {clueId};
        deleteClueByIds(ids);
    }
}
