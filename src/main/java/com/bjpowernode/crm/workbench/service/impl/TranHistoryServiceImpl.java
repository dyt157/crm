package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.pojo.TranHistory;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:TranHistoryServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/25
 */
@Service
public class TranHistoryServiceImpl implements TranHistoryService {
    @Resource
    private TranHistoryMapper tranHistoryMapper;
    @Override
    public int saveTranHistory(TranHistory tranHistory) {
        return tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public List<TranHistory> queryTranHistoryByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryByTranId(tranId);
    }
}
