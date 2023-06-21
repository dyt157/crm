package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.ActivityRemark;

import java.util.List;

/**
 * @Program:ActivityRemarkService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/19
 */
public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkByActivityId(String activityId);

    int saveActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int modifyActivityRemark(ActivityRemark activityRemark);
}
