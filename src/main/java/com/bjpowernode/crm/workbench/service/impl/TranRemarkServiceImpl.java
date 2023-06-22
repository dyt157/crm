package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.pojo.TranRemark;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:TranRemarkServiceimpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class TranRemarkServiceImpl implements TranRemarkService {
    @Resource
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public int saveTranRemarkList(List<TranRemark> tranRemarkList) {
        return tranRemarkMapper.insertTranRemarkList(tranRemarkList);
    }
}
