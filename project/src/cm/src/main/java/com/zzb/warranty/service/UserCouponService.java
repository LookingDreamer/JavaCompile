package com.zzb.warranty.service;

import com.zzb.warranty.model.UserCoupon;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
public interface UserCouponService {

    void insertUserCoupon(UserCoupon userCoupon);

    UserCoupon getUserCouponByCouponCode(String couponCode);

    /**
     * 获取用户奖券
     *
     * @param userId
     * @return
     */
    List<UserCoupon> getUserCouponsByUserCode(String userId);

    /**
     * 获取userCoupon, 每一个userCoupon有一个唯一的code
     *
     * @param couponCode
     * @param userCode
     * @return
     */
    UserCoupon getUserCoupon(String couponCode, String userCode);

    void useUserCoupon(String couponCode, String userCode, String orderNo, String productCode);

    List<UserCoupon> getUserCouponsPageable(Map<String, Object> query);

    Long getUserCouponCount(Map<String, Object> query);
}
