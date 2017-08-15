package com.zzb.warranty.service;

import com.zzb.warranty.model.Coupon;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/2/8.
 */
@Service
public interface CouponService {

    void insertCoupon(Coupon coupon);
}
