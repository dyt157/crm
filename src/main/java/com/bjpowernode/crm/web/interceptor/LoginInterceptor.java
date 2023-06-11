package com.bjpowernode.crm.web.interceptor;

import com.bjpowernode.crm.commons.constant.Constant;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Program:GlobalInterceptor
 * @Description: TODO 全局拦截器，拦截任何业务资源的访问
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //尝试从session域或者Cookie中获取用户信息，能否获取到
        //从Cookie中获取的过程较为繁琐，我们这里就试着获取Session域中的数据
        Object user = request.getSession().getAttribute(Constant.SESSION_USER);
        if (user==null){
            //没有登录，跳转到登录页面（或者首页）
            System.out.println("没有登录");
            System.out.println(request.getContextPath());
            response.sendRedirect(request.getContextPath());
            //返回false，表示该请求被拦截
            return false;
        }
        //程序能到这里，说明用户已经登录，可以放行
        return true;
    }
}
