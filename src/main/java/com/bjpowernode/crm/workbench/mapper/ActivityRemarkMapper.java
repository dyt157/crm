package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.ActivityRemark;

import java.util.List;

/**
 * @Program:ActivityRemarkMapper
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/19
 */
public interface ActivityRemarkMapper {

    List<ActivityRemark> selectActivityRemarkByActivityId(String activityId);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int updateActivityRemark(ActivityRemark activityRemark);
}
