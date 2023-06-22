package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.pojo.Tran;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Program:TranServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranMapper tranMapper;
    @Override
    public int saveTran(Tran tran) {
        return tranMapper.insertTran(tran);
    }
}
