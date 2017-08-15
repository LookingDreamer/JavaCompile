package com.zzb.warranty.service.impl;

import com.zzb.warranty.dao.CouponDao;
import com.zzb.warranty.model.Coupon;
import com.zzb.warranty.service.CouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/2/8.
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Resource
    private CouponDao couponMapper;

    @Override
    public void insertCoupon(Coupon coupon) {
        couponMapper.insertCoupon(coupon);
    }
}
