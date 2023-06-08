package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

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
    @RequestMapping("/toIndex")
    public ModelAndView toIndex(){
        //查询出所有用户（tbl_user）返回给index页面
        List<User> users = userService.queryAllUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userList",users);
        modelAndView.setViewName("workbench/activity/index");

        return modelAndView;
    }
}
