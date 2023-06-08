package com.bjpowernode.crm;

import com.bjpowernode.crm.setting.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * @Program:UserServiceTest
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
public class UserServiceTest {

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        UserService userServiceImpl = applicationContext.getBean("userServiceImpl", UserService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("loginAct","zs");
        map.put("loginPwd","123456");
        System.out.println(userServiceImpl.queryUserByActAndPwd(map));
    }
}
