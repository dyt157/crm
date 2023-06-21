package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ClueActivityRelationServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/19
 */
@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Resource
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public int saveClueActivityRelationList(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.insertClueActivityRelationList(clueActivityRelationList);
    }

    @Override
    public List<ClueActivityRelation> queryClueActivityRelationListByClueId(String clueId) {
        return clueActivityRelationMapper.selectClueActivityRelationListByClueId(clueId);
    }

    @Override
    public ClueActivityRelation queryClueActivityRelationByClueIdAndActivityId(String clueId, String activityId) {
        return clueActivityRelationMapper.selectClueActivityRelationByClueIdAndActivityId(clueId,activityId);
    }

    @Override
    public int releaseContact(String clueId, String activityId) {
        return clueActivityRelationMapper.deleteClueActivityRelationByClueIdAndActivityId(clueId,activityId);
    }
}
