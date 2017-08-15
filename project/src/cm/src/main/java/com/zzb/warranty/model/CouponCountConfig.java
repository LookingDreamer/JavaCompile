package com.zzb.warranty.model;

/**
 * Created by Administrator on 2017/2/8.
 */
public class CouponCountConfig {
    private String couponId;
    private Long count;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
