package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.CustomerRemark;
import java.util.List;

public interface CustomerRemarkMapper {
    int deleteByPrimaryKey(String id);

    int insertCustomerRemark(CustomerRemark customerRemark);
    int insertCustomerRemarkList(List<CustomerRemark> customerRemarkList);

    CustomerRemark selectByPrimaryKey(String id);

    List<CustomerRemark> selectAll();

    int updateByPrimaryKey(CustomerRemark row);
}