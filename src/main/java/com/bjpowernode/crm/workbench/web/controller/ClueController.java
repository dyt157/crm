package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.DictionaryValue;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.DictionaryValueService;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @Program:ClueController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource
    private UserService userService;
    @Resource
    private DictionaryValueService dictionaryValueService;
    @Resource
    private ClueService clueService;

    @RequestMapping("/toIndex")
    public String toIndex(Model model){
        //所有者、称呼、线索状态、线索来源 是动态
        //所有者
        List<User> users = userService.queryAllUser();
        model.addAttribute("userList",users);
        //称呼
        List<DictionaryValue> appellationList = dictionaryValueService.queryDicValueByTypeCode("appellation");
        model.addAttribute("appellationList",appellationList);
        //线索状态
        List<DictionaryValue> clueStateList = dictionaryValueService.queryDicValueByTypeCode("clueState");
        model.addAttribute("clueStateList",clueStateList);
        //线索来源
        List<DictionaryValue> sourceList = dictionaryValueService.queryDicValueByTypeCode("source");
        model.addAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }
    @RequestMapping("/saveClue")
    @ResponseBody
    public Object saveClue(HttpSession session,Clue clue){
        //完善线索信息
        //id
        clue.setId(UUIDUtils.getId());
        //创建人
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setCreateBy(user.getId());
        //创建时间
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        //调用service层插入数据
        int count = clueService.saveClue(clue);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }
        return returnObject;
    }
    @RequestMapping("/queryClueForPage")
    @ResponseBody
    public Object queryClueForPage(Integer pageNum,Integer pageSize){
        PageInfo<Clue> cluePageInfo = clueService.queryClueForPage(pageNum, pageSize,5);
        ReturnObject returnObject = new ReturnObject();
        if (cluePageInfo==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(cluePageInfo);
        }
        return returnObject;
    }

    @RequestMapping("/queryClueById")
    @ResponseBody
    public Object queryClueById(String id){
        Clue clue = clueService.queryClueById(id);
        ReturnObject returnObject = new ReturnObject();
        if (clue==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(clue);
        }
        return returnObject;
    }

    @RequestMapping("/modifyClue")
    @ResponseBody
    public Object modifyClue(HttpSession session,Clue clue){
        //编辑人
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setEditBy(user.getId());
        //编辑时间
        clue.setEditTime(DateUtils.formatDateTime(new Date()));
        int count = clueService.modifyClue(clue);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }
        return returnObject;
    }

    @RequestMapping("/deleteClueByIds")
    @ResponseBody
    public Object deleteClueByIds(String[] ids){
        int count = clueService.deleteClueByIds(ids);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }
        return returnObject;
    }

    @RequestMapping("/queryClueByCondition")
    @ResponseBody
    public Object queryClueByCondition(Integer pageNum,Integer pageSize,Clue clue){
        PageInfo<Clue> cluePageInfo = clueService.queryClueByCondition(pageNum, pageSize, clue);
        System.out.println("|||||||||||||||||||");
        System.out.println(cluePageInfo);
        ReturnObject returnObject = new ReturnObject();
        if (cluePageInfo==null||cluePageInfo.getList().size()==0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("无数据");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(cluePageInfo);
        }
        return returnObject;
    }
}
