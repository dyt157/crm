package com.bjpowernode.crm;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFCellUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Program:ActivityExcelTest
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/11
 */
public class ActivityExcelTest {


    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext =  new ClassPathXmlApplicationContext("spring.xml");
        ActivityService activityService = applicationContext.getBean("activityServiceImpl", ActivityService.class);


        //用户点击批量导出按钮，后台查询出所有的活动列表
        //并且把这些数据写入到一个excel文件中
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("活动列表");
        HSSFRow row = sheet.createRow(0);
        //设置单元格样式（以居中为例）
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//得到一个”居中“的单元格样式对象
        //哪个单元格需要这个样式就拿去使用即可
        HSSFCell cell0 = row.createCell(0);
        cell0.setCellStyle(cellStyle);
        cell0.setCellValue("活动名称");
        row.createCell(1).setCellValue("活动所有者");
        row.createCell(2).setCellValue("活动开始日期");
        row.createCell(3).setCellValue("活动结束日期");
        row.createCell(4).setCellValue("活动花费/元");
        row.createCell(5).setCellValue("活动描述");
        List<Activity> activities = activityService.queryAllActivity();
        for (int i = 0; i < activities.size(); i++) {
            HSSFRow rowData = sheet.createRow(i + 1);
            rowData.createCell(0).setCellValue(activities.get(i).getName());
            rowData.createCell(1).setCellValue(activities.get(i).getOwner());
            rowData.createCell(2).setCellValue(activities.get(i).getStartDate());
            rowData.createCell(3).setCellValue(activities.get(i).getEndDate());
            rowData.createCell(4).setCellValue(activities.get(i).getCost());
            rowData.createCell(5).setCellValue(activities.get(i).getDescription());

        }
        FileOutputStream fos = new FileOutputStream("D:\\java\\java框架\\CRM项目\\activity.xls");
        workbook.write(fos);
        fos.close();




    }

    @Test
    public void testInsertActivityByFile(){
        //把excel中的数据解析出来，插入到数据库表中
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("D:\\java\\java框架\\CRM项目\\serverDir\\活动测试文件.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            //获取该文件的最后一个页码（如果文件有两页，则这个值是：1）
            int activeSheetIndex = workbook.getActiveSheetIndex();
            for (int i = 0; i <=activeSheetIndex; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                //得到这一页的最后一行的行号
                int lastRowNum = sheet.getLastRowNum();
                for (int j = 0; j <= lastRowNum; j++) {
                    HSSFRow row = sheet.getRow(j);
                    //获取到该行的 最后一个单元格的格号+1，也就是总格数
                    //很神奇，上面的lastRowNum就是最后一行的行号
                    //而lastCellNum却是最后一个单元格的格号+1
                    short lastCellNum = row.getLastCellNum();
                    for (int k = 0; k < lastCellNum; k++) {//所以这里需要注意：是<号 不是<=
                        HSSFCell cell = row.getCell(k);
                        //需要根据单元格的数据类型，调用不同的get方法获取单元格的值
                        //不能都以字符串的形式获取，否则会报错（Cannot get a STRING value from a NUMERIC cell）
                        if (cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                            System.out.println(cell.getNumericCellValue());
                        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
                            System.out.print(cell.getStringCellValue()+" ");
                        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
                            System.out.print(cell.getBooleanCellValue()+" ");
                        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
                            System.out.println(cell.getCellFormula()+" ");
                        }else{
                            System.out.println("");
                        }
                    }
                    System.out.println();
                }
                System.out.println("-------------------------------------------------");

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

//    @Test
//    public void testInsertActivityByFile2(){
//        //把excel中的数据解析出来，插入到数据库表中
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream("D:\\java\\java框架\\CRM项目\\serverDir\\活动测试文件.xls");
//            HSSFWorkbook workbook = new HSSFWorkbook(fis);
//            //获取该文件的最后一页的页码（如果文件有两页，则这个值是：1）
//            int activeSheetIndex = workbook.getActiveSheetIndex();
//
//            //创建集合，接收数据
//            List<Activity> activityList = new ArrayList<>();
//            for (int i = 0; i <=activeSheetIndex; i++) {
//                HSSFSheet sheet = workbook.getSheetAt(i);
//                //得到这一页的最后一行的行号
//                int lastRowNum = sheet.getLastRowNum();
//
//                for (int j = 1; j <= lastRowNum; j++) { //数据行从第二行开始
//                    HSSFRow row = sheet.getRow(j);
//                    //获取到该行的 最后一个单元格的格号+1，也就是总格数
//                    //很神奇，上面的lastRowNum就是最后一行的行号
//                    //而lastCellNum却是最后一个单元格的格号+1
//                    short lastCellNum = row.getLastCellNum();
//                    Activity activity = new Activity();
//                    activity.setId(UUIDUtils.getId());
//                    activity.setOwner("xxxxxxxxxxxxx");
//                    activity.setCreateBy("张三");
//                    activity.setCreateTime(DateUtils.formatDateTime(new Date()));
//                    for (int k = 0; k < lastCellNum; k++) {//所以这里需要注意：是<号 不是<=
//                        HSSFCell cell = row.getCell(k);
//                        //需要根据单元格的数据类型，调用不同的get方法获取单元格的值
//                        //不能都以字符串的形式获取，否则会报错（Cannot get a STRING value from a NUMERIC cell）
//                        String cellValue = HSSFCellUtils.getCellValue(cell);
//                        if (k==0){
//                            activity.setName(cellValue);
//                        } else if (k == 1) {
//                            activity.setStartDate(cellValue);
//                        } else if (k == 2) {
//                            activity.setEndDate(cellValue);
//                        } else if (k == 3) {
//                            activity.setCost(cellValue);
//                        } else if (k == 4) {
//                            activity.setDescription(cellValue);
//                        }
//                    }
//                    activityList.add(activity);
//
//                }
//
//            }
//
//            //调用Mapper层插入数据
//            ApplicationContext applicationContext =  new ClassPathXmlApplicationContext("spring.xml");
//            ActivityService activityService = applicationContext.getBean("activityServiceImpl", ActivityService.class);
//            int count = activityService.saveActivityByList(activityList);
//            System.out.println("成功插入"+count+"条记录");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }finally {
//            if (fis!=null){
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//
//
//    }


}
