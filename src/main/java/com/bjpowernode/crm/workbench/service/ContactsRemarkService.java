package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ContactsRemark;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Program:ContactsRemarkService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */

public interface ContactsRemarkService {

    int saveContactsRemark(ContactsRemark contactsRemark);
    int saveContactsRemarkList(List<ContactsRemark> contactsRemarkList);
}
