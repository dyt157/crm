package com.bjpowernode.crm.setting.service.impl;

import com.bjpowernode.crm.setting.mapper.DictionaryTypeMapper;
import com.bjpowernode.crm.setting.pojo.DictionaryType;
import com.bjpowernode.crm.setting.service.DictionaryTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:DictionaryTypeServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
@Service
public class DictionaryTypeServiceImpl implements DictionaryTypeService {
    @Resource
    private DictionaryTypeMapper dictionaryTypeMapper;
    @Override
    public List<DictionaryType> queryAllDictionaryType() {
        return dictionaryTypeMapper.selectAllDictionaryType();
    }

    @Override
    public DictionaryType queryDicTypeByCode(String code) {
        return dictionaryTypeMapper.selectDicTypeByCode(code);
    }

    @Override
    public int saveDicType(DictionaryType dictionaryType) {
        return dictionaryTypeMapper.insertDicType(dictionaryType);
    }
}
