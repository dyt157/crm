package com.bjpowernode.crm.setting.web.controller;

import com.bjpowernode.crm.commons.constant.Constant;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.setting.pojo.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @Program:UserController
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
@Controller
//Controller类的创建需要对应一个原则：一个资源目录对应一个Controller类
///也就是一个控制器类中所有的方法处理完业务后所跳转的资源都在同一个目录下
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin")
    public String toLogin(HttpServletRequest request){
        //尝试获取Cookie,获取到后可以直接跳转到工作台首页
        String loginActCookieVal= "";
        String loginPwdCookieVal="";
        if (request.getCookies()!=null){
            for (Cookie cookie : request.getCookies()) {
                if ("loginAct".equals(cookie.getName())){

                    loginActCookieVal = cookie.getValue();
                    continue;
                }
                if ("loginPwd".equals(cookie.getName())){
                    loginPwdCookieVal = cookie.getValue();
                }
            }
            if (!loginActCookieVal.equals("")&&!loginPwdCookieVal.equals("")){
                //说明账号密码的cookie都能拿到，直接跳转到工作台的首页即可
                HashMap<String, String> map = new HashMap<>();
                map.put("loginAct",loginActCookieVal);
                map.put("loginPwd",loginPwdCookieVal);
                request.getSession().setAttribute("user",userService.queryUserByActAndPwd(map));
                return "redirect:/workbench/toIndex";
            }
        }
        return  "settings/qx/user/login"; //这里不用加'/'是因为视图解析器中已经加了
    }

    @RequestMapping("/settings/qx/user/login")//处理器处理完后，跳转的资源是原来的登录页面
    @ResponseBody
    //不建议直接返回字符串（message），这样返回信息不够清晰，并且容易乱码
    public Object login(String loginAct, String loginPwd, Boolean isRemAP,
                        HttpServletRequest request,
                        HttpServletResponse response){
        HashMap<String, String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userService.queryUserByActAndPwd(map);
        //根据返回的user对象进行判断,并把返回信息封装到一个专门的对象中
        ReturnObject returnObject = new ReturnObject();
        if (user==null){
            //说明没有查询出来，表示用户名或者密码错误
            returnObject.setCode(Constant.RETURN_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");

        }else{//说明查询出来一个用户，但还要继续验证
            //过期时间和当前时间做比较
            String nowDateStr = DateUtils.formatDate(new Date());
            if (nowDateStr.compareTo(user.getExpireTime())>0){
                //当前时间>过期时间，代表账号已过期
                returnObject.setCode(Constant.RETURN_CODE_FAIL);
                returnObject.setMessage("用户名已过期");
            }else if ("0".equals(user.getLockState())){//锁定状态判断
                returnObject.setCode(Constant.RETURN_CODE_FAIL);
                returnObject.setMessage("用户已被锁定");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){//客户端ip地址检查
                returnObject.setCode(Constant.RETURN_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else{
                //登录成功
                returnObject.setCode(Constant.RETURN_CODE_SUCCESS);
                //把user对象存放到session域中
                request.getSession().setAttribute("user",user);

                //登录成功，判断用户是否需要免登录
                if (isRemAP){
                    //用户需要免登录，创建Cookie并让浏览器保存起来
                    Cookie loginActCookie = new Cookie("loginAct", user.getLoginAct());
                    Cookie loginPwdCookie = new Cookie("loginPwd", user.getLoginPwd());
                    loginActCookie.setMaxAge(60*60*24);
                    loginPwdCookie.setMaxAge(60*60*24);
                    loginActCookie.setPath("/");
                    loginPwdCookie.setPath("/");
                    response.addCookie(loginActCookie);
                    response.addCookie(loginPwdCookie);
                }
            }
        }
        return returnObject;
    }

    //处理器方法最终跳转的资源所在目录是/settings/qx/user/
    //最终的url就是 ：/settings/qx/user/+起一个资源名（和方法名最好一致）
    @RequestMapping("/settings/qx/user/logout")
    public String logout(HttpServletResponse response, HttpSession session){

        //"清除"Cookie
        Cookie loginActCookie = new Cookie("loginAct", "");
        Cookie loginPwdCookie = new Cookie("loginPwd", "");
        loginActCookie.setMaxAge(0);
        loginPwdCookie.setMaxAge(0);
        //这里路径设置也不能漏，否则无法成功清除
        loginActCookie.setPath("/");
        loginPwdCookie.setPath("/");
        response.addCookie(loginActCookie);
        response.addCookie(loginPwdCookie);
        //清除Session域中的对象,避免占内存
        //也可以直接销毁session
        session.invalidate();
        //返回登录首页
        //使用重定向更好，避免用户刷新，频繁执行退出操作
        return "redirect:/settings/qx/user/toLogin";
    }
}
