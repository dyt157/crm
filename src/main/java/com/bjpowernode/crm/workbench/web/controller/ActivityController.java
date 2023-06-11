package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Program:ActivityController
 * @Description: TODO 市场活动处理器
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    @RequestMapping("/toIndex")
    public ModelAndView toIndex(){
        ModelAndView modelAndView = new ModelAndView();
        //查询出所有用户（tbl_user）返回给index页面
        List<User> users = userService.queryAllUser();

        modelAndView.addObject("userList",users);
        modelAndView.setViewName("workbench/activity/index");
        return modelAndView;
    }


    @RequestMapping("/saveActivity")
    @ResponseBody
    public Object saveActivity(Activity activity, HttpServletRequest request){
        //完善活动信息
        //添加id，使用UUID生成主键
        activity.setId(UUIDUtils.getId());
        //创建人和创建时间，创建人指的是当前登录的账号的名字
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USER);
        //创建人应该取的是该用户的id
        String createBy = user.getId();
        activity.setCreateBy(createBy);
        //创建时间，即当前系统时间
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));

        System.out.println(activity);

        //调用业务层，添加活动信息到数据库
        int count = activityService.saveActivity(activity);
        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("保存失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("保存成功");
        }
        return returnObject;
    }

    /*@RequestMapping("/getActivityListByPage")
    @ResponseBody
    public Object getActivityListByPage(Integer pageNum,Integer pageSize){
        //实现分页功能，查询出指定页码的数据返回给市场活动的首页
        PageInfo<Activity> page = activityService.queryActivityListByPage(pageNum, pageSize);

        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        if (page==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("查询用户列表失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("查询用户列表成功");
            returnObject.setReturnData(page);
        }
        return returnObject;

    }*/

    @RequestMapping("/queryActivityForConditionByPage")
    @ResponseBody
    public Object queryActivityForConditionByPage(Integer pageNum,Integer pageSize,Activity activity){

        //实现分页功能，查询出指定页码的数据返回给市场活动的首页
        PageInfo<Activity> page = activityService.queryActivityForCondition(pageNum, pageSize,activity);

        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        if (page==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("根据条件查询用户列表失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("根据条件查询用户列表成功");
            returnObject.setReturnData(page);
        }
        return returnObject;

    }

    @RequestMapping("/deleteActivityByIds")
    @ResponseBody
    public Object deleteActivityByIds(String[] ids){
        System.out.println(Arrays.toString(ids));
        int count = activityService.deleteActivityByIds(ids);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("删除成功");
        }
        return returnObject;
    }

    @RequestMapping("/modifyActivity")
    @ResponseBody
    public Object modifyActivity(Activity activity){
        int count = activityService.modifyActivity(activity);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("更新成功");
        }

        return returnObject;

    }

    @RequestMapping("/queryActivityById")
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        ReturnObject returnObject = new ReturnObject();
        if (activity==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activity);
        }

        return returnObject;
    }


}
