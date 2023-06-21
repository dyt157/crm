package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.ContactsActivityRelation;
import java.util.List;

public interface ContactsActivityRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(ContactsActivityRelation row);

    ContactsActivityRelation selectByPrimaryKey(String id);

    List<ContactsActivityRelation> selectAll();

    int updateByPrimaryKey(ContactsActivityRelation row);
}