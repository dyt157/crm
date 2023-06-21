package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.customerRemark;
import java.util.List;

public interface customerRemarkMapper {
    int deleteByPrimaryKey(String id);

    int insert(customerRemark row);

    customerRemark selectByPrimaryKey(String id);

    List<customerRemark> selectAll();

    int updateByPrimaryKey(customerRemark row);
}