package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.NumberUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.pojo.Tran;
import com.bjpowernode.crm.workbench.pojo.TranHistory;
import com.bjpowernode.crm.workbench.pojo.TranStage;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @Program:TranServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/21
 */
@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranMapper tranMapper;

    @Resource
    private TranHistoryMapper tranHistoryMapper;
    @Override
    public int saveTran(Tran tran) {
        return tranMapper.insertTran(tran);
    }

    @Override
    public List<Tran> queryTranForPage(Integer pageNum, Integer pageSize) {
        Integer index = (pageNum-1)*pageSize;
        return tranMapper.selectTranForPage(index,pageSize);
    }

    @Override
    public Integer queryTranCount() {
        return tranMapper.selectTranCount();
    }

    @Override
    public Tran queryTranById(String id) {
        return tranMapper.selectTranById(id);
    }

    @Override
    public int modifyTranStage(Tran tran) {
        return tranMapper.updateTranStage(tran);
    }

    @Override
    @Transactional
    public Object modifyTranStageInDetail(User user, Tran tran) {
        String dateTime = DateUtils.formatDateTime(new Date());
        //根据交易id查询出原来交易的信息
        Tran oldTran =queryTranById(tran.getId());
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtils.getId());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(dateTime);
        tranHistory.setMoney(oldTran.getMoney());
        tranHistory.setTranId(oldTran.getId());
        tranHistory.setStage(oldTran.getStage());
        tranHistory.setExpectedDate(oldTran.getExpectedDate());
        //调用Service层保存数据到tbl_tran_history表中
        int tranHistoryCount = tranHistoryMapper.insertTranHistory(tranHistory);
        tranHistory.setCreateBy(user.getName());
        tranHistory.setMoney(NumberUtils.convertThousandth(tranHistory.getMoney()));
        //完善当前tran对象
        tran.setEditBy(user.getId());
        tran.setEditTime(dateTime);
        //更新数据库表中的数据
        int tranCount = modifyTranStage(tran);
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
        return returnObject;
    }

    @Override
    public List<TranStage> queryTranStageCount() {
        return tranMapper.selectPerStageCount();
    }
}
