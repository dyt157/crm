package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Program:WorkbenchIndexController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
@Controller
public class WorkbenchIndexController {
    @RequestMapping("/workbench/toIndex")
    public String toIndex(){
        return "workbench/index";
    }
}
