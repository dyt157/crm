package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ClueRemark;

import java.util.List;

/**
 * @Program:ClueRemarkService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/18
 */
public interface ClueRemarkService {

    int saveClueRemark(ClueRemark clueRemark);

    List<ClueRemark> queryClueRemarkByClueId(String clueId);
    List<ClueRemark> queryClueRemarkByClueIdForConvert(String clueId);

    int modifyClueRemark(ClueRemark clueRemark);

    int deleteClueRemarkById(String id);

    int deleteClueRemarkByClueId(String clueId);
}
