package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFCellUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Program:ActivityServiceImpl
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;
    @Override
    public int saveActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public PageInfo<Activity> queryActivityListByPage(Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize);
        //查询全部记录即可，分页插件会拦截你的sql进行处理，查询出来的结果集是分页后的结果集
        List<Activity> activities = activityMapper.selectAllActivity();
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public PageInfo<Activity> queryActivityForCondition(Integer pageNum, Integer pageSize, Activity activity) {

        PageHelper.startPage(pageNum,pageSize);

        List<Activity> activities = activityMapper.selectActivityForCondition(activity);
        PageInfo<Activity> pageInfo = new PageInfo<>(activities);
        return pageInfo;
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public int modifyActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public List<Activity> queryAllActivity() {
        return activityMapper.selectAllActivity();
    }

    @Override
    public void createActivityFile() {
        //查询出所有活动列表信息
        List<Activity> activities = queryAllActivity();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动第一页");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("活动名称");
        row.createCell(1).setCellValue("活动所有者");
        row.createCell(2).setCellValue("活动开始日期");
        row.createCell(3).setCellValue("活动结束日期");
        row.createCell(4).setCellValue("活动花费/元");
        row.createCell(5).setCellValue("活动描述");
        for (int i = 0; i < activities.size(); i++) {
            HSSFRow rowData = sheet.createRow(i + 1);
            rowData.createCell(0).setCellValue(activities.get(i).getName());
            rowData.createCell(1).setCellValue(activities.get(i).getOwner());
            rowData.createCell(2).setCellValue(activities.get(i).getStartDate());
            rowData.createCell(3).setCellValue(activities.get(i).getEndDate());
            rowData.createCell(4).setCellValue(activities.get(i).getCost());
            rowData.createCell(5).setCellValue(activities.get(i).getDescription());
        }

        OutputStream fos = null;
        try {
            fos = new FileOutputStream("D:\\java\\java框架\\CRM项目\\市场活动.xls");
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void createActivityFileByIds(String[] ids) {
        //调用Mapper层，根据所选的ids查询出活动列表数据
        List<Activity> activities = activityMapper.selectActivityByIds(ids);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动第一页");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("活动名称");
        row.createCell(1).setCellValue("活动所有者");
        row.createCell(2).setCellValue("活动开始日期");
        row.createCell(3).setCellValue("活动结束日期");
        row.createCell(4).setCellValue("活动花费/元");
        row.createCell(5).setCellValue("活动描述");
        for (int i = 0; i < activities.size(); i++) {
            HSSFRow rowData = sheet.createRow(i + 1);
            rowData.createCell(0).setCellValue(activities.get(i).getName());
            rowData.createCell(1).setCellValue(activities.get(i).getOwner());
            rowData.createCell(2).setCellValue(activities.get(i).getStartDate());
            rowData.createCell(3).setCellValue(activities.get(i).getEndDate());
            rowData.createCell(4).setCellValue(activities.get(i).getCost());
            rowData.createCell(5).setCellValue(activities.get(i).getDescription());
        }

        OutputStream fos = null;
        try {
            fos = new FileOutputStream("D:\\java\\java框架\\CRM项目\\市场活动_select.xls");
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    @Transactional
    public int saveActivityByList(List<Activity> activities) {
        return activityMapper.insertActivityByList(activities);
    }

    @Override
    public Activity queryActivityByIdConvertOwner(String id) {
        return activityMapper.selectActivityByIdConvertOwner(id);
    }

    @Override
    public List<Activity> queryActivityByName(String name) {
        return activityMapper.selectActivityByName(name);
    }

    @Override
    public List<Activity> queryActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }


}
