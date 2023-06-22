package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.workbench.pojo.Clue;
import com.bjpowernode.crm.workbench.pojo.Tran;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Program:ClueService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/16
 */
public interface ClueService{

    int saveClue(Clue clue);

    PageInfo<Clue> queryClueForPage(Integer pageNum, Integer pageSize, Integer navigatePages);

    Clue queryClueById(String id);

    int modifyClue(Clue clue);

    int deleteClueByIds(String[] ids);

    PageInfo<Clue> queryClueByCondition(Integer pageNum,Integer pageSize,Clue clue);

    Clue queryClueByIdToDetail(String id);

    void clueConvert(String clueId, User user, Tran tran, boolean isCreateTran);
}
