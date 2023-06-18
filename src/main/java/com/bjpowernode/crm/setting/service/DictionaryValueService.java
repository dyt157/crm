package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.setting.pojo.DictionaryValue;

import java.util.List;

/**
 * @Program:DictionaryValueService
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/16
 */
public interface DictionaryValueService {

    List<DictionaryValue> queryDicValueByTypeCode(String typeCode);
}
