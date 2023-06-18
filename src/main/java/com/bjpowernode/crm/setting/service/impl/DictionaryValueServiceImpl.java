package com.bjpowernode.crm.setting.service.impl;

import com.bjpowernode.crm.setting.mapper.DictionaryValueMapper;
import com.bjpowernode.crm.setting.pojo.DictionaryValue;
import com.bjpowernode.crm.setting.service.DictionaryValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Program:DictionaryValueServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/16
 */
@Service
public class DictionaryValueServiceImpl  implements DictionaryValueService {
    @Resource
    private DictionaryValueMapper dictionaryValueMapper;
    @Override
    public List<DictionaryValue> queryDicValueByTypeCode(String typeCode) {
        return dictionaryValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
