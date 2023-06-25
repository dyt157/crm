package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.TranHistory;

import java.util.List;

/**
 * @Program:TranHistoryService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/25
 */
public interface TranHistoryService {

    int saveTranHistory(TranHistory tranHistory);
    List<TranHistory> queryTranHistoryByTranId(String tranId);
}
