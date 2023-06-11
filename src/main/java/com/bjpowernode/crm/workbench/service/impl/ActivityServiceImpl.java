package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ActivityServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;
    @Override
    public int saveActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public PageInfo<Activity> queryActivityListByPage(Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize);
        //查询全部记录即可，分页插件会拦截你的sql进行处理，查询出来的结果集是分页后的结果集
        List<Activity> activities = activityMapper.selectAllActivity();
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public PageInfo<Activity> queryActivityForCondition(Integer pageNum, Integer pageSize, Activity activity) {

        PageHelper.startPage(pageNum,pageSize);

        List<Activity> activities = activityMapper.selectActivityForCondition(activity);
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public int modifyActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }
}
