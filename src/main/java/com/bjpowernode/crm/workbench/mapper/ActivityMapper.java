package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Program:ActivityMapper
 * @Description: TODO 市场活动CRUD
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
public interface ActivityMapper {

    int insertActivity(Activity activity);

    List<Activity> selectActivityListByPage(@Param("index") Integer index,@Param("pageSize") Integer pageSize);
    List<Activity> selectAllActivity();

    List<Activity> selectActivityForCondition(Activity activity);

    int deleteActivityByIds(String[] ids);

    int updateActivity(Activity activity);

    Activity selectActivityById(String id);

    List<Activity> selectActivityByIds(String[] ids);

    int insertActivityByList(List<Activity> activities);

    Activity selectActivityByIdConvertOwner(String id);

    List<ActivityRemark> selectActivityRemarkByActivityId(String activityId);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int updateActivityRemark(ActivityRemark activityRemark);
}
