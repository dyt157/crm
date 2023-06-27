package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Tran;
import com.bjpowernode.crm.workbench.pojo.TranStage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranMapper {
    int deleteByPrimaryKey(String id);

    int insertTran(Tran tran);

    Tran selectTranById(String id);

    List<Tran> selectTranForPage(Integer index,Integer pageSize);

    int updateByPrimaryKey(Tran row);

    Integer selectTranCount();

    int updateTranStage(Tran tran);

    List<TranStage> selectPerStageCount();
}