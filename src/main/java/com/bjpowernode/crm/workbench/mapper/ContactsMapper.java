package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Contacts;
import java.util.List;

public interface ContactsMapper {
    int deleteByPrimaryKey(String id);

    int insertContacts(Contacts contacts);

    Contacts selectByPrimaryKey(String id);

    List<Contacts> selectAll();

    int updateByPrimaryKey(Contacts row);
}