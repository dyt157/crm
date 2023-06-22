package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ContactsRemarkMapper;
import com.bjpowernode.crm.workbench.pojo.ContactsRemark;
import com.bjpowernode.crm.workbench.service.ContactsRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ContactsRemarkServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class ContactsRemarkServiceImpl implements ContactsRemarkService {
    @Resource
    private ContactsRemarkMapper contactsRemarkMapper;
    @Override
    public int saveContactsRemark(ContactsRemark contactsRemark) {
        return contactsRemarkMapper.insertContactsRemark(contactsRemark);
    }

    @Override
    public int saveContactsRemarkList(List<ContactsRemark> contactsRemarkList) {
        return contactsRemarkMapper.insertContactsRemarkList(contactsRemarkList);
    }
}
