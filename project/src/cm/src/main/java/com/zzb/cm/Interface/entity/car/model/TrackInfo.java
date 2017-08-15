package com.zzb.cm.Interface.entity.car.model;

import java.sql.Timestamp;

/**
 * 任务跟踪轨迹
 * Created by austinChen on 2015/10/9.
 */
public class TrackInfo {

    /*订单序号*/
    private Integer orderNum;
    /*是否显示*/
    private Boolean isShow;
    /**/
    private Timestamp opTime;
    /*处理人*/
    private String operator;
    /*动作*/
    private String action;
    /*原始状态*/
    private String oldState;
    /*当前状态*/
    private String newState;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean isShow() {
        return isShow;
    }

    public void setShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public String getOldState() {
        return oldState;
    }

    public void setOldState(String oldState) {
        this.oldState = oldState;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Timestamp getOpTime() {
        return opTime;
    }

    public void setOpTime(Timestamp opTime) {
        this.opTime = opTime;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
