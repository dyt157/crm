package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.pojo.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ContactsServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class ContactsServiceImpl implements ContactsService {
    @Resource
    private ContactsMapper contactsMapper;
    @Override
    public int saveContacts(Contacts contacts) {
        return contactsMapper.insertContacts(contacts);
    }

    @Override
    public List<Contacts> queryContactsByName(String name) {
        return contactsMapper.selectContactsByName(name);
    }
}
