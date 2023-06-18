package com.bjpowernode.crm.setting.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.setting.pojo.DictionaryType;
import com.bjpowernode.crm.setting.service.DictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Program:DictionaryTypeController
 * @Description: TODO 数据字典类型处理器
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
@Controller
@RequestMapping("/settings/dictionary/type")
public class DictionaryTypeController {
    @Autowired
    private DictionaryTypeService dictionaryTypeService;

    @RequestMapping("/toIndex")
    public String toIndex(Model model){
        List<DictionaryType> dictionaryTypes = dictionaryTypeService.queryAllDictionaryType();
        model.addAttribute("dictionaryTypes",dictionaryTypes);
        return "settings/dictionary/type/index";
    }

    @RequestMapping("/toSaveDictionaryType")
    public String toSaveDictionaryType(){
        return "settings/dictionary/type/save";
    }

    @RequestMapping("/queryDicTypeByCode")
    @ResponseBody
    public Object queryDicTypeByCode(String code){
        DictionaryType dictionaryType = dictionaryTypeService.queryDicTypeByCode(code);
        ReturnObject returnObject = new ReturnObject();
        if (dictionaryType!=null){
            //已经存在的dicType对象
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }
        return returnObject;
    }
    @RequestMapping("/saveDicType")
    public String saveDicType(DictionaryType dictionaryType){
        dictionaryTypeService.saveDicType(dictionaryType);
        return "redirect:/settings/dictionary/type/toIndex";
    }



}
