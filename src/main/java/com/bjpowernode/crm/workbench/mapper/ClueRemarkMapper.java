package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.ClueRemark;

import java.util.List;

/**
 * @Program:ClueRemarkMapper
 * @Description: TODO 线索备注mapper
 * @Author: Mr.deng
 * @DATE: 2023/6/18
 */
public interface ClueRemarkMapper {

    int insertClueRemark(ClueRemark clueRemark);

    List<ClueRemark> selectClueRemarkByClueId(String clueId);

    int updateClueRemark(ClueRemark clueRemark);

    int deleteClueRemarkById(String id);
}
