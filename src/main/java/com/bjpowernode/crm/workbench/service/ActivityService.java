package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Activity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Program:ActivityService
 * @Description: TODO 市场活动业务接口
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
public interface ActivityService {

    int saveActivity(Activity activity);

    PageInfo<Activity> queryActivityListByPage(Integer pageNum, Integer pageSize);

    PageInfo<Activity> queryActivityForCondition(Integer pageNum, Integer pageSize,Activity activity);

    int deleteActivityByIds(String[] ids);

    int modifyActivity(Activity activity);

    Activity queryActivityById(String id);
}
