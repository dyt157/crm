package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Program:ClueActivityRelationMapper
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/19
 */
public interface ClueActivityRelationMapper {

    int insertClueActivityRelationList(List<ClueActivityRelation> clueActivityRelationList);

    List<ClueActivityRelation> selectClueActivityRelationListByClueId(String clueId);

    ClueActivityRelation selectClueActivityRelationByClueIdAndActivityId(@Param("clueId") String clueId,@Param("activityId") String activityId);

    int deleteClueActivityRelationByClueIdAndActivityId(String clueId,String activityId);
}
