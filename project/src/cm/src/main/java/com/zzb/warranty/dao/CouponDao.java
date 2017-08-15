package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.Coupon;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface CouponDao extends BaseDao<Coupon> {

    Coupon getCouponById(String couponId);

    List<Coupon> getCoupons();

    void insertCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);
}
