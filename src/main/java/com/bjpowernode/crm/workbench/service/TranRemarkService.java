package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.TranRemark;

import java.util.List;

/**
 * @Program:TranRemarkService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
public interface TranRemarkService {

    int saveTranRemarkList(List<TranRemark> tranRemarkList);
}
