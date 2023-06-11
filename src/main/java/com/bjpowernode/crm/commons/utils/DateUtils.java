package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Program:DateUtils
 * @Description: TODO 日期处理类
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
public class DateUtils {

    public static String formatDateTime(Date date){
        // HH：24小时
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDateStr = sdf.format(date);
        return nowDateStr;
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateStr = sdf.format(date);
        return nowDateStr;
    }
}
