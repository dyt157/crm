package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFCellUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.pojo.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Program:ActivityController
 * @Description: TODO 市场活动处理器
 * @Author: Mr.deng
 * @DATE: 2023/6/8
 */
@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityRemarkService activityRemarkService;

    /**
     * 跳转到活动首页
     * @return
     */
    @RequestMapping("/toIndex")
    public ModelAndView toIndex(){
        ModelAndView modelAndView = new ModelAndView();
        //查询出所有用户（tbl_user）返回给index页面
        List<User> users = userService.queryAllUser();

        modelAndView.addObject("userList",users);
        modelAndView.setViewName("workbench/activity/index");
        return modelAndView;
    }

    /**
     * 保存活动信息
     * @param activity 前端发送过来的数据
     * @param request
     * @return
     */
    @RequestMapping("/saveActivity")
    @ResponseBody
    public Object saveActivity(Activity activity, HttpServletRequest request){
        //完善活动信息
        //添加id，使用UUID生成主键
        activity.setId(UUIDUtils.getId());
        //创建人和创建时间，创建人指的是当前登录的账号的名字
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USER);
        //创建人应该取的是该用户的id
        String createBy = user.getId();
        activity.setCreateBy(createBy);
        //创建时间，即当前系统时间
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));

        System.out.println(activity);

        //调用业务层，添加活动信息到数据库
        int count = activityService.saveActivity(activity);
        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("保存失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("保存成功");
        }
        return returnObject;
    }

    /*@RequestMapping("/getActivityListByPage")
    @ResponseBody
    public Object getActivityListByPage(Integer pageNum,Integer pageSize){
        //实现分页功能，查询出指定页码的数据返回给市场活动的首页
        PageInfo<Activity> page = activityService.queryActivityListByPage(pageNum, pageSize);

        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        if (page==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("查询用户列表失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("查询用户列表成功");
            returnObject.setReturnData(page);
        }
        return returnObject;

    }*/

    /**
     * 根据条件查询活动列表，并进行分页处理
     * @param pageNum
     * @param pageSize
     * @param activity
     * @return
     */
    @RequestMapping("/queryActivityForConditionByPage")
    @ResponseBody
    public Object queryActivityForConditionByPage(Integer pageNum,Integer pageSize,Activity activity){

        //实现分页功能，查询出指定页码的数据返回给市场活动的首页
        PageInfo<Activity> page = activityService.queryActivityForCondition(pageNum, pageSize,activity);

        //创建返回对象
        ReturnObject returnObject = new ReturnObject();
        if (page==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("根据条件查询用户列表失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("根据条件查询用户列表成功");
            returnObject.setReturnData(page);
        }
        return returnObject;

    }

    @RequestMapping("/deleteActivityByIds")
    @ResponseBody
    public Object deleteActivityByIds(String[] ids){
        System.out.println(Arrays.toString(ids));
        int count = activityService.deleteActivityByIds(ids);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("删除成功");
        }
        return returnObject;
    }

    @RequestMapping("/modifyActivity")
    @ResponseBody
    public Object modifyActivity(HttpSession session,Activity activity){
        //编辑人
        User user = (User) session.getAttribute("user");
        activity.setEditBy(user.getId());
        //编辑时间
        activity.setEditTime(DateUtils.formatDateTime(new Date()));

        int count = activityService.modifyActivity(activity);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setMessage("更新成功");
        }

        return returnObject;

    }

    @RequestMapping("/queryActivityById")
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        ReturnObject returnObject = new ReturnObject();
        if (activity==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activity);
        }

        return returnObject;
    }

    @RequestMapping("/activityListDownLoad")
    public void activityListDownLoad(HttpServletResponse response){

        //调用Service层的方法，在服务器的硬盘上生成一个对应的excel文件
        activityService.createActivityFile();

        //1、设置响应信息的数据类型
        //作用：告诉浏览器你的响应信息是什么类型，浏览器就会采用对应的方式去解析，处理
        //你返回的响应体是什么数据类型，那么setContentType()方法就要指明对应的类型
        //不然浏览器解析不了，无法处理
        response.setContentType("application/vnd.ms-excel"); //xls文件对应的MIME数据类型
        //用这个也行，application/octet-stream，几乎是万能的
        //response.setContentType("application/octet-stream;charset=utf-8");//二进制流文件

        //2、设置响应头,浏览器默认情况下会打开响应信息，我们不想让浏览器打开
        //Content-Disposition:表示内容的处理方式  attachment:附件
        //表示让浏览器对该响应信息采用附件的方式，也就是下载的方式
        //还可以指定文件名，如果文件名含有中文，记得还要指定编码格式
        try {
            response.setHeader("Content-Disposition",
                    "attachment;fileName="+ URLEncoder.encode("市场活动.xls","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //3、把服务器中生成的excel文件写给客户端
        FileInputStream fis = null;
        try {
            //通过响应对象生成字节输出流，这种输出流专门负责响应给客户端
            ServletOutputStream os = response.getOutputStream();
            //创建输入流，先读取服务器上的文件，在通过输出流响应给客户端
            fis = new FileInputStream("D:\\java\\java框架\\CRM项目\\市场活动.xls");
            byte[] bytes = new byte[256];
            int read=fis.read(bytes);
            while (read!=-1){
                os.write(bytes,0,read);
                read = fis.read(bytes);
            }
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    @RequestMapping("/activityListDownLoadByIds")
    //由于前端提交的数据是：id=xxx&id=xxx 所以形参名也得是id
    public void activityListDownLoadByIds(HttpServletResponse response,String[] id){

        //调用Service层的方法，在服务器端生成一个对应的excel文件
        activityService.createActivityFileByIds(id);

        //1、设置响应信息的数据类型
        response.setContentType("application/vnd.ms-excel"); //xls文件对应的MIME数据类型
        //用这个也行，application/octet-stream，几乎是万能的
        //response.setContentType("application/octet-stream;charset=utf-8");//二进制流文件

        //2、设置响应头,浏览器默认情况下会打开响应信息，我们不想让浏览器打开
        //Content-Disposition:表示内容的处理方式  attachment:附件
        //表示让浏览器对该响应信息采用附件的方式，也就是下载的方式
        try {
            response.setHeader("Content-Disposition","attachment;fileName="+ URLEncoder.encode("市场活动_select.xls","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //3、把服务器中生成的excel文件写给客户端
        FileInputStream fis = null;
        try {
            //通过响应对象生成字节输出流，这种输出流专门负责响应给客户端
            ServletOutputStream os = response.getOutputStream();
            //创建输入流，先读取服务器上的文件，在通过输出流响应给客户端
            fis = new FileInputStream("D:\\java\\java框架\\CRM项目\\市场活动_select.xls");
            byte[] bytes = new byte[256];
            int read=fis.read(bytes);
            while (read!=-1){
                os.write(bytes,0,read);
                read = fis.read(bytes);
            }
            //最好刷新一下
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    @RequestMapping("/activityListUpLoad")
    @ResponseBody
    public Object activityListUpLoad(MultipartFile activityFile,HttpSession session){
        //springmvc通过配置文件中定义的文件解析器，把用户上传的文件进行解析
        //并把这个文件的信息全部封装到MultipartFile对象中
        //可见 MultipartFile对象的创建需要依赖另一个对象（文件解析器对象）
        InputStream is = null;
        try {
            //第一种方式
            //以下两步是把发送过来的文件内容，输出到服务器上的一个文件上
            //File file = new File("D:\\java\\java框架\\CRM项目\\serverDir\\活动测试文件.xls");
            //activityFile.transferTo(file);
            //is = new FileInputStream("D:\\java\\java框架\\CRM项目\\serverDir\\活动测试文件.xls");
            //HSSFWorkbook workbook = new HSSFWorkbook(is);

            //第二种方式,不在服务器上创建一个文件了，直接让activityFile对象和HSSFWorkbook对象“连接”
            //这个输入流是 连通activityFile对象和其它对象的关键
            is = activityFile.getInputStream();
            //HSSFWorkbook workbook = new HSSFWorkbook(is);
            //通过activityFile对象得到的输入流传给 HSSFWorkbook类的构造方法后
            //通过输入流获取到activityFile对象的信息，借此创建出来的HSSFWorkbook对象就包含了
            //用户上传的文件的全部信息
            //is = new FileInputStream("D:\\java\\java框架\\CRM项目\\serverDir\\活动测试文件.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            //获取该文件的最后一页的页码（如果文件有两页，则这个值是：1）
            int activeSheetIndex = workbook.getActiveSheetIndex();
            //创建集合，接收数据
            List<Activity> activities = new ArrayList<>();
            for (int i = 0; i <=activeSheetIndex; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                //得到这一页的最后一行的行号
                int lastRowNum = sheet.getLastRowNum();

                for (int j = 1; j <= lastRowNum; j++) { //数据行从第二行开始
                    HSSFRow row = sheet.getRow(j);
                    //获取到该行的 最后一个单元格的格号+1，也就是总格数
                    //很神奇，上面的lastRowNum就是最后一行的行号
                    //而lastCellNum却是最后一个单元格的格号+1
                    short lastCellNum = row.getLastCellNum();

                    //每一行记录对应一个activity对象
                    Activity activity = new Activity();
                    activity.setId(UUIDUtils.getId());
                    //在这里我们所有者就取当前登录的用户了
                    User user = (User) session.getAttribute("user");
                    activity.setOwner(user.getId());
                    activity.setCreateBy(user.getId());
                    activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                    for (int k = 0; k < lastCellNum; k++) {//所以这里需要注意：是<号 不是<=
                        HSSFCell cell = row.getCell(k);
                        //需要根据单元格的数据类型，调用不同的get方法获取单元格的值
                        //不能都以字符串的形式获取，
                        //否则会报错（Cannot get a STRING value from a NUMERIC cell）
                        String cellValue = HSSFCellUtils.getCellValue(cell);
                        if (k==0){
                            activity.setName(cellValue);
                        } else if (k == 1) {
                            activity.setStartDate(cellValue);
                        } else if (k == 2) {
                            activity.setEndDate(cellValue);
                        } else if (k == 3) {
                            activity.setCost(cellValue);
                        } else if (k == 4) {
                            activity.setDescription(cellValue);
                        }
                    }
                    activities.add(activity);

                }
            }

            //调用Service层，实现数据的插入
            int count = activityService.saveActivityByList(activities);
            ReturnObject returnObject = new ReturnObject();
            if (count<0){
                returnObject.setCode(Constant.RETURN_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请重新尝试....");
            }else{
                returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
                returnObject.setMessage("保存成功");
                returnObject.setReturnData(count);
            }
            return returnObject;

        } catch (IOException e) {
                throw new RuntimeException(e);
        }finally{
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }




    @RequestMapping("/toDetail")
    @ResponseBody
    public ModelAndView toDetail(String id){

        ModelAndView modelAndView = new ModelAndView();
        //查询活动详细信息
        Activity activity = activityService.queryActivityByIdConvertOwner(id);
        modelAndView.addObject("activity",activity);
        modelAndView.setViewName("workbench/activity/detail");
        return modelAndView;
    }

    @RequestMapping("/queryActivityRemark")
    @ResponseBody
    public Object queryActivityRemark(String activityId){
        List<ActivityRemark> activityRemarks = activityRemarkService.queryActivityRemarkByActivityId(activityId);

        ReturnObject returnObject = new ReturnObject();
        if (activityRemarks==null||activityRemarks.size()==0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(activityRemarks);
        }
        return returnObject;
    }

    @RequestMapping("/saveActivityRemark")
    @ResponseBody
    public Object saveActivityRemark(HttpSession session,ActivityRemark activityRemark){
        //前端只发送了活动id以及备注内容，我们还需要完善activityRemark对象
        //id
        activityRemark.setId(UUIDUtils.getId());
        //创建人
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        activityRemark.setCreateBy(user.getId());
        //创建时间
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));

        //调用Service层，插入数据
        int count = activityRemarkService.saveActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }

        return returnObject;

    }
    @RequestMapping("/deleteActivityRemarkById")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        int count = activityRemarkService.deleteActivityRemarkById(id);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else {
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);

        }
        return returnObject;
    }

    @RequestMapping("/queryUserById")
    @ResponseBody
    public Object queryUserById(String id){
        User user = userService.queryUserById(id);
        ReturnObject returnObject = new ReturnObject();
        if (user==null){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
            returnObject.setReturnData(user.getName());
        }
        return returnObject;
    }

    /**
     * 更新备注信息
     * @param activityRemark
     * @return
     */
    @RequestMapping("/modifyActivityRemark")
    @ResponseBody
    public Object modifyActivityRemark(HttpSession session,ActivityRemark activityRemark){
        //完善activityRemark对象
        //编辑人id
        activityRemark.setEditBy(((User)session.getAttribute(Constant.SESSION_USER)).getId());
        //编辑时间
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        //编辑状态
        activityRemark.setEditFlag("1");
        int count = activityRemarkService.modifyActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if (count<=0){
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage(Constant.RETURN_MESSAGE_FAIL);
        }else{
            returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
        }
        return returnObject;

    }
}
