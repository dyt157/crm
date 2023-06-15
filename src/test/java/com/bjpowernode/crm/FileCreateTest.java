package com.bjpowernode.crm;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
 * @Program:FileCreateTest
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/11
 */
public class FileCreateTest {

    public static void main(String[] args) throws IOException {
        //创建一个带有数据的excel文件
        //先有文件,再有页数,行，单元格（列）
        //创建一个 HSSFWorkbook对象--->一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //基于HSSFWorkbook对象创建一个 HSSFSheet对象--->一页
        //参数就是 该页的名字
        HSSFSheet sheet = workbook.createSheet("学生列表");
        //基于HSSFSheet对象创建一个 HSSFRow对象--->一行
        //参数代表 行号，从0开始，0代表第一行
        HSSFRow row = sheet.createRow(0);
        //基于HSSFRow对象创建一个 HSSFCell对象--->该行中的一个单元格
        //参数代表 单元格号，从0开始，0代表第一个单元格
        HSSFCell cell = row.createCell(0);
        //给这个单元格赋值
        cell.setCellValue("编号");
        //设置单元格样式（以居中为例），这个不是必须的
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//得到一个”居中“的单元格样式对象
        //哪个单元格需要这个样式就拿去使用即可
        cell.setCellStyle(cellStyle);

        //创建这一行的所有单元格并赋值
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");
        //第一行的数据设置完毕
        //第二行开始就是实际的数据
        for (int i = 1; i <=10; i++) {
            HSSFRow rowData = sheet.createRow(i);
            rowData.createCell(0).setCellValue(100+i);
            rowData.createCell(1).setCellValue("张三"+i+"号");
            rowData.createCell(2).setCellValue(19);
        }

        //设置完数据后，就可以把数据往某个文件写了
        //现在这些数据将来在运行时，只在java程序中，我们需要把这些数据写入到某个文件中
        //自然是通过输出流去完成
        FileOutputStream fos = new FileOutputStream("D:\\java\\java框架\\CRM项目\\studentList.xls");
        workbook.write(fos);
    }
}
