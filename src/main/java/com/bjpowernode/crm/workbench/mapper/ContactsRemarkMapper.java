package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.ContactsRemark;
import java.util.List;

public interface ContactsRemarkMapper {
    int deleteByPrimaryKey(String id);

    int insertContactsRemark(ContactsRemark contactsRemark);

    int insertContactsRemarkList(List<ContactsRemark> contactsRemarkList);

    ContactsRemark selectByPrimaryKey(String id);

    List<ContactsRemark> selectAll();

    int updateByPrimaryKey(ContactsRemark row);
}