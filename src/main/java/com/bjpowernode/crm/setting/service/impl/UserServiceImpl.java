package com.bjpowernode.crm.setting.service.impl;

import com.bjpowernode.crm.setting.mapper.UserMapper;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Program:UserServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByActAndPwd(Map<String, String> map) {
        return userMapper.selectUserByActAndPwd(map);
    }

    @Override
    public List<User> queryAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public User queryUserById(String id) {
        return userMapper.selectUserById(id);
    }
}
