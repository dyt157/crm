package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.TranRemark;
import java.util.List;

public interface TranRemarkMapper {
    int deleteByPrimaryKey(String id);

    int insertTranRemark(TranRemark tranRemark);
    int insertTranRemarkList(List<TranRemark> tranRemarkList);

    TranRemark selectByPrimaryKey(String id);

    List<TranRemark> selectAll();

    int updateByPrimaryKey(TranRemark row);
}