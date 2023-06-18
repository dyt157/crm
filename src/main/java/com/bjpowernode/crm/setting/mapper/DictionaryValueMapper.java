package com.bjpowernode.crm.setting.mapper;

import com.bjpowernode.crm.setting.pojo.DictionaryValue;

import java.util.List;

/**
 * @Program:DictionaryValueMapper
 * @Description: TODO 字典值mapper
 * @Author: Mr.deng
 * @DATE: 2023/6/16
 */
public interface DictionaryValueMapper {

    List<DictionaryValue> selectDicValueByTypeCode(String typeCode);
}
