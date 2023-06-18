package com.bjpowernode.crm.setting.pojo;

/**
 * @Program:DictionaryType
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/15
 */
public class DictionaryType {

    private String code;
    private String name ;
    private String description;

    @Override
    public String toString() {
        return "DictionaryType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
