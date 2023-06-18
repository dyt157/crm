package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.setting.mapper.DictionaryTypeMapper;
import com.bjpowernode.crm.setting.pojo.DictionaryType;

import java.util.List;

/**
 * @Program:DictionaryTypeService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
public interface DictionaryTypeService {

    List<DictionaryType> queryAllDictionaryType();

    DictionaryType queryDicTypeByCode(String code);

    int saveDicType(DictionaryType dictionaryType);
}
