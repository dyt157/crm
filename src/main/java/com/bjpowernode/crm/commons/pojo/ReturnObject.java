package com.bjpowernode.crm.commons.pojo;

/**
 * @Program:ReturnObject
 * @Description: TODO 专门处理返回信息的类
 * @Author: Mr.deng
 * @DATE: 2023/6/7
 */
public class ReturnObject {

    private String code;//成功或者失败的代号
    private String message;//对于成功或者失败的描述信息
    private Object returnDate;//其他的一些返回数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Object returnDate) {
        this.returnDate = returnDate;
    }

    public ReturnObject() {
    }

    public ReturnObject(String code, String message, Object returnDate) {
        this.code = code;
        this.message = message;
        this.returnDate = returnDate;
    }
}
