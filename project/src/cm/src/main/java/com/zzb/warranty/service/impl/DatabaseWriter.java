package com.zzb.warranty.service.impl;

import com.zzb.warranty.dao.UserCouponDao;
import com.zzb.warranty.model.UserCoupon;

/**
 * Created by Administrator on 2017/2/10.
 */
class DatabaseWriter implements Runnable {

    private UserCouponDao userCouponDao;

    private UserCoupon userCoupon;

    DatabaseWriter(UserCouponDao userCouponDao, UserCoupon userCoupon) {
        this.userCouponDao = userCouponDao;
        this.userCoupon = userCoupon;
    }

    @Override
    public void run() {
        userCouponDao.insertUserCoupon(userCoupon);
    }
}
