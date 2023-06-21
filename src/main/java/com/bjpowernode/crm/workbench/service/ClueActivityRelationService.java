package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

/**
 * @Program:ClueActivityRelationService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/19
 */
public interface ClueActivityRelationService {

    int saveClueActivityRelationList(List<ClueActivityRelation> clueActivityRelationList);

    List<ClueActivityRelation> queryClueActivityRelationListByClueId(String clueId);

    ClueActivityRelation queryClueActivityRelationByClueIdAndActivityId(String clueId,String activityId);

    int releaseContact(String clueId,String activityId);
}
