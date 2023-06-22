package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.bjpowernode.crm.workbench.pojo.ContactsActivityRelation;
import com.bjpowernode.crm.workbench.service.ContactsActivityRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ContactsActivityRelationServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {
    @Resource
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Override
    public int saveContactsActivityRelationList(List<ContactsActivityRelation> contactsActivityRelationList) {
        return contactsActivityRelationMapper.insertContactsActivityRelationList(contactsActivityRelationList);
    }
}
