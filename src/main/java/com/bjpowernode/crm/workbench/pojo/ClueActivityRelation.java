package com.bjpowernode.crm.workbench.pojo;

/**
 * @Program:ClueActivityRelation
 * @Description: TODO
 * @Author: Mr.deng
 * @DATE: 2023/6/19
 */
public class ClueActivityRelation {
    private String id;
    private String clueId;
    private String ActivityId;

    @Override
    public String toString() {
        return "ClueActivityRelation{" +
                "id='" + id + '\'' +
                ", clueId='" + clueId + '\'' +
                ", ActivityId='" + ActivityId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }
}
