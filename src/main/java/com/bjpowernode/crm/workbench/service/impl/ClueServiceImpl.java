package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.pojo.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ClueServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/16
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueMapper clueMapper;
    @Override
    public int saveClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public PageInfo<Clue> queryClueForPage(Integer pageNum, Integer pageSize, Integer navigatePages) {
        PageHelper.startPage(pageNum,pageSize);
        List<Clue> clues = clueMapper.selectAllClue();
        PageInfo<Clue> cluePageInfo = new PageInfo<>(clues,navigatePages);
        return cluePageInfo;
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int modifyClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public PageInfo<Clue> queryClueByCondition(Integer pageNum,Integer pageSize,Clue clue) {
        PageHelper.startPage(pageNum,pageSize);
        List<Clue> clues = clueMapper.selectClueByCondition(clue);
        System.out.println("------------------");
        System.out.println(clues);
        PageInfo<Clue> cluePageInfo = new PageInfo<>(clues, 5);
        return cluePageInfo;
    }
}
