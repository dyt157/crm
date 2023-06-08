package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Program:MainIndexController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */

@Controller
public class MainIndexController {
    @RequestMapping("/workbench/main/toMainIndex")
    public String toMainIndex(){
        return "workbench/main/index";
    }
}
