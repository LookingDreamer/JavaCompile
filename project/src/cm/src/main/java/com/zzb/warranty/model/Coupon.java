package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/7.
 */
public class Coupon implements Identifiable {
    private String id;
    private Double amount;
    private String description;
    private Date effectiveTime;
    private Date expireTime;
    private Date createTime;
    private Date modifyTime;
    private String operatorId;
    private String operatorName;
    private Long couponCount;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Long couponCount) {
        this.couponCount = couponCount;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
