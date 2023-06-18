package com.bjpowernode.crm.setting.mapper;

import com.bjpowernode.crm.setting.pojo.DictionaryType;

import java.util.List;

/**
 * @Program:DictionaryTypeMapper
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
public interface DictionaryTypeMapper {

    List<DictionaryType> selectAllDictionaryType();

    DictionaryType selectDicTypeByCode(String code);

    int insertDicType(DictionaryType dictionaryType);
}
