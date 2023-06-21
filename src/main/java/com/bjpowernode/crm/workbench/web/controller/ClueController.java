package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.DictionaryValue;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.DictionaryValueService;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.Clue;
import com.bjpowernode.crm.workbench.pojo.ClueActivityRelation;
import com.bjpowernode.crm.workbench.pojo.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.github.pagehelper.PageInfo;
import org.omg.CORBA.UserException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    @Resource
    private ClueRemarkService clueRemarkService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ClueActivityRelationService clueActivityRelationService;

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

    @RequestMapping("/toDetail")
    public ModelAndView toDetail(String id){
        ModelAndView modelAndView = new ModelAndView();
        //查询线索详细信息
        Clue clue = clueService.queryClueByIdToDetail(id);
        //查询线索相关的备注
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkByClueId(id);
        //查询关联的市场活动
        //先从关联表中查询出对应的活动id
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationService.queryClueActivityRelationListByClueId(id);
        //再根据活动列表的id查询出关联的活动
        List<Activity> activityList = new ArrayList<>();
        if (clueActivityRelations.size()>0){
            String[] ids=new String[clueActivityRelations.size()];
            for (int i = 0; i < clueActivityRelations.size(); i++) {
                ids[i]=clueActivityRelations.get(i).getActivityId();
            }
            activityList = activityService.queryActivityByIds(ids);
        }
        //设置共享域内容
        modelAndView.addObject("clue",clue);
        modelAndView.addObject("clueRemarkList",clueRemarkList);
        modelAndView.addObject("activityList",activityList);
        modelAndView.setViewName("workbench/clue/detail");
        return modelAndView;
    }

    @RequestMapping("/saveClueRemark")
    @ResponseBody
    public Object saveClueRemark(HttpSession session,ClueRemark clueRemark){
        //前端只发送了noteContent和ClueId
        //id
        clueRemark.setId(UUIDUtils.getId());
        //创建人
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clueRemark.setCreateBy(user.getId());
        //创建时间
        clueRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        //编辑人和编辑时间不需要设置

        //调用service层插入数据
        int count = clueRemarkService.saveClueRemark(clueRemark);
        //插入之后，重新设置clueRemark对象中创建人的值
        clueRemark.setCreateBy(user.getName());

        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            //这一步是关键  把这个对象返回前端，前端把数据拼接到备注信息栏下
            returnObject.setReturnData(clueRemark);
        }
        return returnObject;

    }

    @RequestMapping("/modifyClueRemark")
    @ResponseBody
    public Object modifyClueRemark(HttpSession session,ClueRemark clueRemark){
        //完善对象
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        clueRemark.setEditFlag("1");

        //更新备注信息
        int count = clueRemarkService.modifyClueRemark(clueRemark);
        ReturnObject returnObject = new ReturnObject();
        //把编辑人id替换为编辑人的名字,因为前端需要展示编辑人，不可能展示一个id
        clueRemark.setEditBy(user.getName());
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(clueRemark);
        }
        return returnObject;
    }

    @RequestMapping("/deleteClueRemarkById")
    @ResponseBody
    public Object deleteClueRemarkById(String id){
        int count = clueRemarkService.deleteClueRemarkById(id);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }

        return returnObject;
    }
    @RequestMapping("/queryActivityByName")
    @ResponseBody
    public Object queryActivityByName(String name){
        List<Activity> activityList = activityService.queryActivityByName(name);
        ReturnObject returnObject = new ReturnObject();
        if (activityList==null||activityList.size()==0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activityList);
        }
        return returnObject;

    }

    @RequestMapping("/saveClueActivityRelation")
    @ResponseBody
    public Object saveClueActivityRelation(String clueId,String[] activityIds){
        ArrayList<ClueActivityRelation> clueActivityRelations = new ArrayList<>();
        for (int i = 0; i < activityIds.length; i++) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtils.getId());
            clueActivityRelation.setActivityId(activityIds[i]);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelations.add(clueActivityRelation);
        }


        int count = clueActivityRelationService.saveClueActivityRelationList(clueActivityRelations);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else {
            //为了前端拼接数据，需要把关联成功的市场活动列返回
            List<Activity> activityList = activityService.queryActivityByIds(activityIds);
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activityList);
        }
        return returnObject;
    }

    @RequestMapping("/queryClueActivityRelationByClueIdAndActivityId")
    @ResponseBody
    public Object queryClueActivityRelationByClueIdAndActivityId(String clueId,String activityId){
        ClueActivityRelation clueActivityRelation = clueActivityRelationService.queryClueActivityRelationByClueIdAndActivityId(clueId, activityId);
        ReturnObject returnObject = new ReturnObject();
        if (clueActivityRelation==null){
            //说明这条市场活动没有被关联,是可以添加关联关系的
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }else{
            //已经存在关联关系了
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;

    }

    @RequestMapping("/releaseContact")
    @ResponseBody
    public Object releaseContact(String clueId,String activityId){
        int count = clueActivityRelationService.releaseContact(clueId, activityId);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }
        return returnObject;



    }

    //-----------线索转换

    @RequestMapping("/toConvert")
    public ModelAndView toConvert(String clueId){
        ModelAndView modelAndView = new ModelAndView();
        //根据clueId,查询出线索进行返回
        Clue clue = clueService.queryClueByIdToDetail(clueId);
        //查询出字典类型是"stage"的字典值
        List<DictionaryValue> stageList = dictionaryValueService.queryDicValueByTypeCode("stage");
        modelAndView.addObject("clue",clue);
        modelAndView.addObject("stageList",stageList);
        modelAndView.setViewName("workbench/clue/convert");
        return modelAndView;
    }


}
