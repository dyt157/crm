package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.workbench.pojo.Contacts;
import org.springframework.stereotype.Service;

/**
 * @Program:ContactsService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */

public interface ContactsService {

    int saveContacts(Contacts contacts);
}
