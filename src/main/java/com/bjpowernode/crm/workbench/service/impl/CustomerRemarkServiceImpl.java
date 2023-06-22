package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.CustomerRemarkMapper;
import com.bjpowernode.crm.workbench.pojo.CustomerRemark;
import com.bjpowernode.crm.workbench.service.CustomerRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:CustomerRemarkServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Resource
    private CustomerRemarkMapper customerRemarkMapper;
    @Override
    public int saveCustomerRemark(CustomerRemark customerRemark) {
        return customerRemarkMapper.insertCustomerRemark(customerRemark);
    }

    @Override
    public int saveCustomerRemarkList(List<CustomerRemark> customerRemarkList) {
        return customerRemarkMapper.insertCustomerRemarkList(customerRemarkList);
    }
}
