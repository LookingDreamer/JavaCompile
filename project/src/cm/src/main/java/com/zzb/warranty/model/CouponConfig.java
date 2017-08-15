package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/8.
 */
public class CouponConfig implements Identifiable {
    private String id;

    private String key;

    private String value;

    private Date startTime;

    private Date endTime;

    private Date deadline;

    private Integer defaultCouponTimes;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getDefaultCouponTimes() {
        return defaultCouponTimes;
    }

    public void setDefaultCouponTimes(Integer defaultCouponTimes) {
        this.defaultCouponTimes = defaultCouponTimes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
