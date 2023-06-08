package com.bjpowernode.crm.setting.mapper;

import com.bjpowernode.crm.setting.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @Program:UserMapper
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
public interface UserMapper {
    /**
     *根据账户名密码查询用户
     * @param map
     * @return
     */
    User selectUserByActAndPwd(Map<String,String> map);

    List<User> selectAllUser();
}
