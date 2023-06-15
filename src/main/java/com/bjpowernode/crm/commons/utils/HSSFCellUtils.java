package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * @Program:HSSFCellUtils
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/13
 */
public class HSSFCellUtils {

    /**
     * 专门获取单元格的值
     *
     * @return 统一返回字符串即可，因为字符串数据可以直接插入到数据库表中
     *         并且表中的数据的类型都是char或者varchar，我们实体类里的属性的类型也是String
     */
    public static String getCellValue(HSSFCell cell){
        if (cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            return cell.getNumericCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            return cell.getStringCellValue();
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            return cell.getBooleanCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            return cell.getCellFormula()+"";
        }else{
            return "";
        }
    }
}
