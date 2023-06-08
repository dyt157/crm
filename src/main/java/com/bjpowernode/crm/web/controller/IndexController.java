package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Program:IndexController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String toIndex(){
        return "/index";
    }
}
