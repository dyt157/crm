package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.pojo.ClueRemark;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:ClueRemarkServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/18
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Resource
    private ClueRemarkMapper clueRemarkMapper;
    @Override
    public int saveClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }

    @Override
    public List<ClueRemark> queryClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkByClueId(clueId);
    }

    @Override
    public List<ClueRemark> queryClueRemarkByClueIdForConvert(String clueId) {
        return clueRemarkMapper.selectClueRemarkByClueIdForConvert(clueId);
    }

    @Override
    public int modifyClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.updateClueRemark(clueRemark);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueRemarkById(id);
    }

    @Override
    public int deleteClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.deleteClueRemarkByClueId(clueId);
    }
}
