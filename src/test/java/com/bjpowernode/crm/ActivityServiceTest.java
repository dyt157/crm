package com.bjpowernode.crm;

import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * @Program:ActivityServiceTest
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/10
 */
public class ActivityServiceTest {

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        ActivityService activityService = applicationContext.getBean("activityServiceImpl", ActivityService.class);
        Activity activity = new Activity();
        activity.setId("1cc3a6ba295449fb9273212d0126a04a");
        activity.setName("百度滴滴");
        int i = activityService.modifyActivity(activity);
        System.out.println("更新了："+i+"行");
    }
}
