package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.NumberUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.DictionaryValue;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.DictionaryValueService;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.pojo.*;
import com.bjpowernode.crm.workbench.service.*;
//import com.sun.org.apache.xpath.internal.operations.String;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Program:TranController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/22
 */
@Controller
@RequestMapping("/workbench/transaction")
public class TranController {
    @Resource
    private UserService userService;
    @Resource
    private DictionaryValueService dictionaryValueService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private CustomerService customerService;
    @Resource
    private TranService tranService;
    @Resource
    private TranRemarkService tranRemarkService;
    @Resource
    private TranHistoryService tranHistoryService;

    @RequestMapping("/toIndex")
    public String toIndex(){
        /*
        //所有者、阶段、类型、来源 都是动态的
        List<User> userList = userService.queryAllUser();
        List<DictionaryValue> stageList = dictionaryValueService.queryDicValueByTypeCode("stage");
        List<DictionaryValue> typeList = dictionaryValueService.queryDicValueByTypeCode("transactionType");
        List<DictionaryValue> sourceList = dictionaryValueService.queryDicValueByTypeCode("source");
        model.addAttribute("userList",userList);
        model.addAttribute("stageList",stageList);
        model.addAttribute("typeList",typeList);
        model.addAttribute("sourceList",sourceList);*/
        return "workbench/transaction/index";

    }
    @RequestMapping("/toSave")
    public String toSave(Model model){
        //所有者、阶段、类型、来源 都是动态的
        List<User> userList = userService.queryAllUser();
        List<DictionaryValue> stageList = dictionaryValueService.queryDicValueByTypeCode("stage");
        List<DictionaryValue> transactionTypeList = dictionaryValueService.queryDicValueByTypeCode("transactionType");
        List<DictionaryValue> sourceList = dictionaryValueService.queryDicValueByTypeCode("source");
        model.addAttribute("userList",userList);
        model.addAttribute("stageList",stageList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        model.addAttribute("sourceList",sourceList);
        return "workbench/transaction/save";

    }
    @RequestMapping("/searchActivityByName")
    @ResponseBody
    public Object searchActivityByName(String name){
        List<Activity> activityList = activityService.queryActivityByName(name);
        ReturnObject returnObject = new ReturnObject();
        if (activityList!=null&&activityList.size()>0){
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activityList);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;
    }

    @RequestMapping("/searchContactsByName")
    @ResponseBody
    public Object searchContactsByName(String fullname){
        List<Contacts> contactList = contactsService.queryContactsByName(fullname);
        ReturnObject returnObject = new ReturnObject();
        if (contactList!=null&&contactList.size()>0){
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(contactList);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;
    }

    @RequestMapping("/queryCustomerNameByName")
    @ResponseBody
    public Object queryCustomerNameByName(String name){
        List<Customer> customerList = customerService.queryCustomerByName(name);
        ReturnObject returnObject = new ReturnObject();
        if (customerList!=null&&customerList.size()>0){
            ArrayList<String> customerNameList = new ArrayList<>();
            for (Customer customer:customerList) {
                customerNameList.add(customer.getName());
            }
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(customerNameList);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;
    }


    @RequestMapping("/queryPossibilityForStage")
    @ResponseBody
    public Object queryPossibilityForStage(String stage) throws IOException {
        //从配置文件中查询不同阶段对应的可能性
        //stage的值在前台已经做了表单验证，不会是空字符串

        //第一种方式:Properties类（JDK早期提供的）
        //Properties properties = new Properties();
        //InputStream inputStream = TranController.class.
        //        getClassLoader().getResourceAsStream("possibility.properties");
        //properties.load(inputStream);
        //String possibility = properties.getProperty(stage);

        //第二种方式：ResourceBundle类（JDK后期提供的）
        //相对路径是从类路径下开始，并且因为这个类专门用于xxx.properties文件
        //所以不需要加后缀名，只需要填写前面的文件名即可
        ResourceBundle resourceBundle = ResourceBundle.getBundle("possibility");
        String possibility = resourceBundle.getString(stage);

        ReturnObject returnObject = new ReturnObject();
        if (possibility!=null&&!possibility.equals("")){
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(possibility);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;
    }

    @RequestMapping("/saveTran")
    public String saveTran(HttpSession session,Tran tran){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        String dateTime = DateUtils.formatDateTime(new Date());
        //完善tran对象
        tran.setId(UUIDUtils.getId());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(dateTime);
        //此时的tran对象中的customerId的值是客户名称，不是id值
        //并且这个客户名称是否在客户表中存在也要验证
        String customerName = tran.getCustomerId();
        Customer customer = customerService.queryCustomerByFullName(customerName);
        if (customer==null){
            //说明该客户在客户表中不存在记录，创建客户
            Customer newCustomer = new Customer();
            newCustomer.setId(UUIDUtils.getId());
            newCustomer.setCreateTime(dateTime);
            newCustomer.setCreateBy(user.getId());
            newCustomer.setOwner(user.getId());
            newCustomer.setName(customerName);
            //创建客户
            customerService.saveCustomer(newCustomer);
            //重新设置customer_id
            tran.setCustomerId(newCustomer.getId());
        }else {
            //已存在客户
            tran.setCustomerId(customer.getId());
        }
        //保存交易
        tranService.saveTran(tran);
        return "workbench/transaction/index";
    }


    @RequestMapping("/queryTranForPage")
    @ResponseBody
    public Object queryTranForPage(Integer pageNum,Integer pageSize){
        List<Tran> tranList = tranService.queryTranForPage(pageNum, pageSize);
        Integer tranCount = tranService.queryTranCount();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("tranCount",tranCount);
        hashMap.put("tranList",tranList);
        ReturnObject returnObject = new ReturnObject();
        if (tranList!=null&&tranList.size()>0){
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(hashMap);
        }else {
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;
    }

    //---------------交易明细模块

    @RequestMapping("/toDetail")
    public ModelAndView toDetail(String tranId){
        Tran tran = tranService.queryTranById(tranId);
        //给money属性的值加入千分位
        tran.setMoney(NumberUtils.convertThousandth(tran.getMoney()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("tran",tran);
        //读取配置文件获取可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        modelAndView.addObject("possibility",possibility);

        //明细界面选需要展示所有的交易阶段信息
        List<DictionaryValue> stageList = dictionaryValueService.queryDicValueByTypeCode("stage");
        modelAndView.addObject("stageList",stageList);

        //把这个交易中的阶段值在整个阶段列表中处于哪个下标查询出来，并转发到明细页面做判断
        int index;
        for (index = 0; index < stageList.size(); index++) {
            if (stageList.get(index).getValue().equals(tran.getStage())){
                break;
            }

        }
        modelAndView.addObject("tranIndex",index);

        //查询交易对应的备注信息
        List<TranRemark> tranRemarkList = tranRemarkService.queryTranRemarkByTranId(tranId);
        modelAndView.addObject("tranRemarkList",tranRemarkList);

        //查询交易对应的阶段历史
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryByTranId(tranId);
        //千分位转换
        for (TranHistory tranHistory:tranHistoryList) {
            tranHistory.setMoney(NumberUtils.convertThousandth(tranHistory.getMoney()));
        }
        modelAndView.addObject("tranHistoryList",tranHistoryList);

        modelAndView.setViewName("workbench/transaction/detail");
        return modelAndView;
    }

    @RequestMapping("/modifyTranStage")
    @ResponseBody
    public Object modifyTranStage(HttpSession session,Tran tran){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        Object returnObject = tranService.modifyTranStageInDetail(user, tran);
        return returnObject;

        /*User user = (User) session.getAttribute(Constant.SESSION_USER);
        String dateTime = DateUtils.formatDateTime(new Date());
        //根据交易id查询出原来交易的信息
        Tran oldTran = tranService.queryTranById(tran.getId());
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtils.getId());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(dateTime);
        tranHistory.setMoney(oldTran.getMoney());
        tranHistory.setTranId(oldTran.getId());
        tranHistory.setStage(oldTran.getStage());
        tranHistory.setExpectedDate(oldTran.getExpectedDate());
        //调用Service层保存数据到tbl_tran_history表中
        int tranHistoryCount = tranHistoryService.saveTranHistory(tranHistory);
        tranHistory.setCreateBy(user.getName());
        tranHistory.setMoney(NumberUtils.convertThousandth(tranHistory.getMoney()));
        //完善当前tran对象
        tran.setEditBy(user.getId());
        tran.setEditTime(dateTime);
        //更新数据库表中的数据
        int tranCount = tranService.modifyTranStage(tran);
        ReturnObject returnObject = new ReturnObject();
        if (tranHistoryCount>0&&tranCount>0){
            //读取配置文件获取可能性
            ResourceBundle bundle = ResourceBundle.getBundle("possibility");
            String possibility = bundle.getString(tran.getStage());

            HashMap<String, Object> tranHashMap = new HashMap<>();
            tran.setEditBy(user.getName());
            tranHashMap.put("tran",tran);
            tranHashMap.put("possibility",possibility);
            tranHashMap.put("tranHistory",tranHistory);
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(tranHashMap);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }
        return returnObject;*/
    }

    @RequestMapping("/queryAllStage")
    @ResponseBody
    public Object queryAllStage(){
        List<DictionaryValue> stageList = dictionaryValueService.queryDicValueByTypeCode("stage");
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        returnObject.setReturnData(stageList);
        return returnObject;
    }

   /* @RequestMapping("/queryTranHistoryByTranId")
    @ResponseBody
    public Object queryTranHistoryByTranId(String tranId){
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryByTranId(tranId);
        ReturnObject returnObject = new ReturnObject();
        if (tranHistoryList!=null&&tranHistoryList.size()>0){
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(tranHistoryList);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }
        return returnObject;
    }*/


}
