package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.CouponTimes;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface UserCouponTimesDao extends BaseDao<CouponTimes> {

    CouponTimes getCouponTimes(String userCode);

    void updateCouponTimes(CouponTimes couponTimes);

    void insertCouponTimes(CouponTimes couponTimes);
}
