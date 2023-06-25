package com.bjpowernode.crm.commons.utils;

/**
 * @Program:NumberUtils
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/25
 */
public class NumberUtils {

    /**
     * 把大于3位数的数字转换为带有千分位的数字，如 5000--->5,000
     * @param number
     * @return
     */
    public static String convertThousandth(String number){
        StringBuilder newNumber = new StringBuilder(number);
        if (newNumber.length()>3){
            for (int i = newNumber.length()-3; i > 0; i-=3) {
                newNumber.insert(i,",");
            }
        }
        return String.valueOf(newNumber);
    }
}
