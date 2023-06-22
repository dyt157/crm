package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Tran;
import java.util.List;

public interface TranMapper {
    int deleteByPrimaryKey(String id);

    int insertTran(Tran tran);

    Tran selectByPrimaryKey(String id);

    List<Tran> selectAll();

    int updateByPrimaryKey(Tran row);
}