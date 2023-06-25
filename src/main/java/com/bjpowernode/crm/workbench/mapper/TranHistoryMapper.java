package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.TranHistory;
import java.util.List;

public interface TranHistoryMapper {
    int deleteByPrimaryKey(String id);

    int insertTranHistory(TranHistory tranHistory);

    TranHistory selectByPrimaryKey(String id);

    List<TranHistory> selectTranHistoryByTranId(String tranId);

    int updateByPrimaryKey(TranHistory row);
}