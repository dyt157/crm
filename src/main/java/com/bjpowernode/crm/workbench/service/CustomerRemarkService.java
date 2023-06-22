package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.CustomerRemark;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Program:CustomerRemarkService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */

public interface CustomerRemarkService {

    int saveCustomerRemark(CustomerRemark customerRemark);
    int saveCustomerRemarkList(List<CustomerRemark> customerRemarkList);
}
