package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Customer;
import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(String id);

    int insertCustomer(Customer customer);

    Customer selectByPrimaryKey(String id);

    List<Customer> selectAll();

    int updateByPrimaryKey(Customer row);

    List<Customer> selectCustomerByName(String name);

    Customer selectCustomerByFullName(String name);
}