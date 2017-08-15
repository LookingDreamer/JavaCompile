package com.zzb.warranty.service;

import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.model.UserCoupon;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface CouponDrawService {

    void start();

    void stop();

    boolean isStarted();

    boolean isStoped();

    void addCouponsToPool();

    /**
     * 用户剩余的抽奖机会
     *
     * @param userId
     * @return 次数
     */
    Integer getCouponChancesLeft(String userId);

    /**
     * 抽奖动作
     *
     * @param userId
     * @return
     */
    UserCoupon drawACoupon(INSBAgent agent);

    /**
     * 通过奖券code获取奖券
     *
     * @param couponCode
     * @return
     */
    UserCoupon getUserCouponByCouponCode(String couponCode);

    /**
     * 获取用户奖券
     *
     * @param userId
     * @return
     */
    List<UserCoupon> getUserCouponsByUserCode(String userId);


    List<UserCoupon> getUserCouponsByProductCode(String userCode, String productCode);

    /**
     * 获取userCoupon, 每一个userCoupon有一个唯一的code
     *
     * @param couponCode
     * @param userCode
     * @return
     */
    UserCoupon getUserCoupon(String couponCode, String userCode);
}
