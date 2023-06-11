package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

/**
 * @Program:UUIDUtils
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
public class UUIDUtils {

    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
