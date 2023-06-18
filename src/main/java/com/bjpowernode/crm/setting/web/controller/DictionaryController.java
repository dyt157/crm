package com.bjpowernode.crm.setting.web.controller;

import com.bjpowernode.crm.commons.pojo.ReturnObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Program:DictionaryController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
@Controller
public class DictionaryController {


    @RequestMapping("/settings/dictionary/toIndex")
    public String toIndex(){
        return "settings/dictionary/index";
    }

}
