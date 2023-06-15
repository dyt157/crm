package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.setting.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @Program:UserService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
public interface UserService {

    User queryUserByActAndPwd(Map<String,String> map);

    List<User> queryAllUser();

    User queryUserById(String id);
}
