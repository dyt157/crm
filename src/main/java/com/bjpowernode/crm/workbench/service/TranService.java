package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.workbench.pojo.Tran;

import java.util.List;

/**
 * @Program:TranService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
public interface TranService {

    int saveTran(Tran tran);

    List<Tran> queryTranForPage(Integer pageNum,Integer pageSize);

    Integer queryTranCount();

    Tran queryTranById(String id);

    int modifyTranStage(Tran tran);

    Object modifyTranStageInDetail(User user,Tran tran);
}
