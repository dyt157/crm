package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Customer;
import org.springframework.stereotype.Service;

/**
 * @Program:CustomerService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */

public interface CustomerService {

    int saveCustomer(Customer customer);
}