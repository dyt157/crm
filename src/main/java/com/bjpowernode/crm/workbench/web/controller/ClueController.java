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
import com.bjpowernode.crm.workbench.pojo.*;
import com.bjpowernode.crm.workbench.service.*;
import com.github.pagehelper.PageInfo;
import org.omg.CORBA.UserException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlAttachmentRef;
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
@Transactional
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
    @Resource
    private ContactsService contactsService;
    @Resource
    private ContactsRemarkService contactsRemarkService;
    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerRemarkService customerRemarkService;
    @Resource
    private ContactsActivityRelationService contactsActivityRelationService;
    @Resource
    private TranService tranService;
    @Resource
    private TranRemarkService tranRemarkService;
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


    @RequestMapping("/clueConvert")//可以在这里声明吗？？
    public String clueConvert(String clueId,HttpSession session,Tran tran,boolean isCreateTran){
        /*//根据线索id查询出该线索的全部原始信息
        Clue clue = clueService.queryClueById(clueId);
        //线索中的信息转化到客户表中
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getId());
        //创建人和创建时间（当前时间点下）
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        String dateTime = DateUtils.formatDateTime(new Date());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(dateTime);
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setWebsite(clue.getWebsite());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getCompany());
        customer.setDescription(clue.getDescription());
        customer.setPhone(clue.getPhone());

        //线索中的信息转换到联系人中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getId());
        contacts.setCustomerId(customer.getId());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(dateTime);

        //调用Service层，插入数据
        customerService.saveCustomer(customer);
        contactsService.saveContacts(contacts);

        int a = 10/0;

        //线索备注信息转换到客户备注表和联系人备注表中
        //这条线索必须有备注信息才进行转换，不然会报错
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkByClueIdForConvert(clueId);
        if (clueRemarks!=null&&clueRemarks.size()>0){
            ArrayList<ContactsRemark> contactsRemarkList = new ArrayList<>();
            ArrayList<CustomerRemark> customerRemarkList = new ArrayList<>();
            for (ClueRemark clueRemark:clueRemarks){
                //联系人备注对象
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setId(UUIDUtils.getId());
                //客户备注对象
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setId(UUIDUtils.getId());

                //添加到集合中
                contactsRemarkList.add(contactsRemark);
                customerRemarkList.add(customerRemark);
            }
            contactsRemarkService.saveContactsRemarkList(contactsRemarkList);
            customerRemarkService.saveCustomerRemarkList(customerRemarkList);
        }
        //把线索和市场活动的关联关系转换到联系人和市场活动的关联关系中
        //这条线索必须有关联的市场活动才进行转换，不然会报错
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationService.queryClueActivityRelationListByClueId(clueId);
        if (clueActivityRelationList!=null&&clueActivityRelationList.size()>0){
            ArrayList<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtils.getId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                //添加到集合中
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            contactsActivityRelationService.saveContactsActivityRelationList(contactsActivityRelationList);
        }


        //创建交易
        if (isCreateTran){//用户在前段点击了创建交易的复选框，
            //我们在转换线索页面进行交易的创建，是一种快速创建方式
            //所以有些数据前端没有发送过来，我们就不用给这些属性赋值，空着就行
            tran.setId(UUIDUtils.getId());
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(dateTime);
            tran.setCustomerId(customer.getId());
            tran.setOwner(user.getId());
            //tran.setDescription();
            //tran.setContactSummary();
            //tran.setSource();
            //tran.setNextContactTime();
            //tran.setType();
            tranService.saveTran(tran);

            //线索的备注转换到交易备注中
            ArrayList<TranRemark> tranRemarkList = new ArrayList<>();
            if (clueRemarks!=null&&clueRemarks.size()>0){
                for (ClueRemark clueRemark:clueRemarks) {
                    TranRemark tranRemark = new TranRemark();
                    tranRemark.setId(UUIDUtils.getId());
                    tranRemark.setCreateBy(clueRemark.getCreateBy());
                    tranRemark.setCreateTime(clueRemark.getCreateTime());
                    tranRemark.setEditBy(clueRemark.getEditBy());
                    tranRemark.setEditTime(clueRemark.getEditTime());
                    tranRemark.setEditFlag(clueRemark.getEditFlag());
                    tranRemark.setNoteContent(clueRemark.getNoteContent());
                    tranRemark.setTranId(tran.getId());
                    tranRemarkList.add(tranRemark);
                }
            }
            tranRemarkService.saveTranRemarkList(tranRemarkList);
        }
        //删除线索备注
        clueRemarkService.deleteClueRemarkByClueId(clueId);
        //删除线索和市场活动之间的关联
        clueActivityRelationService.releaseContact(clueId,null);
        //删除线索
        String[] ids = {clueId};
        clueService.deleteClueByIds(ids);*/

        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clueService.clueConvert(clueId,user,tran,isCreateTran);
        return "forward:/workbench/clue/toIndex";
    }

    @RequestMapping("/queryActivityForTran")
    @ResponseBody
    public Object queryActivityForTran(String clueId,String name){
        //先根据线索id查询出和这条线索有关联的市场活动的id
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationService.queryClueActivityRelationListByClueId(clueId);
        ArrayList<String> activityIdList = new ArrayList<>();
        if (clueActivityRelationList!=null&&clueActivityRelationList.size()>0){
            for (ClueActivityRelation clueActivityRelation:clueActivityRelationList) {
                activityIdList.add(clueActivityRelation.getActivityId());
            }
        }
        //根据活动id和活动名称查询出对应的市场活动列表
        List<Activity> activityList = activityService.queryActivityListByNameAndActivityIdList(name, activityIdList);
        ReturnObject returnObject = new ReturnObject();
        if (activityList!=null&&activityList.size()>0){
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activityList);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);

        }

        return returnObject;
    }

}
