package com.zzb.warranty.service.impl;

import com.zzb.warranty.model.CouponTimes;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface CouponTimesService {
    CouponTimes getCouponTimes(String userCode);

    void updateCouponTimes(CouponTimes couponTimes);

    void insertCouponTimes(CouponTimes couponTimes);
}
