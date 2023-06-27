package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.workbench.pojo.TranStage;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Program:ChartTransaction
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/27
 */
@Controller
@RequestMapping("/workbench/chart/transaction")
public class ChartTransaction {
    @Resource
    private TranService tranService;

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "workbench/chart/transaction/index";
    }
    @RequestMapping("/queryStageCount")
    @ResponseBody
    public Object queryStageCount(){
        List<TranStage> tranStageList = tranService.queryTranStageCount();

        ReturnObject returnObject = new ReturnObject();
        if (tranStageList!=null&&tranStageList.size()>0){
            List<String> tranStageNameList = new ArrayList<>();
            for (TranStage tranStage:tranStageList) {
                tranStageNameList.add(tranStage.getName());
            }
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("tranStageList",tranStageList);
            hashMap.put("tranStageNameList",tranStageNameList);

            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(hashMap);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }
        return returnObject;
    }

}
