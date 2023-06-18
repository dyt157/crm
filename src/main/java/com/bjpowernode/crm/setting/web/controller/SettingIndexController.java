package com.bjpowernode.crm.setting.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Program:DictionaryController
 * @Description: TODO 字典模块的处理器类
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
@Controller
public class SettingIndexController {

    @RequestMapping("/settings/toIndex")
    public String toIndex(){
        return "settings/index";
    }

}
