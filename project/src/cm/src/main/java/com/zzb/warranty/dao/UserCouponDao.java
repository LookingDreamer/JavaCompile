package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.UserCoupon;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface UserCouponDao extends BaseDao<UserCoupon> {

    UserCoupon getUserCouponByCouponCode(String couponCode);

    UserCoupon getUserCoupon(String couponCode, String userCode);

    void insertUserCoupon(UserCoupon userCoupon);

    void removeUserCoupon(String couponCode);

    List<UserCoupon> getUserCoupons(String userCode);

    List<UserCoupon> getUserCouponsByProduct(String userCode, String productCode);

    List<UserCoupon> getUserCoupons(Map<String, Object> query);

    void updateUserCoupon(UserCoupon userCoupon);

    /**
     * 设置订单被用
     *
     * @param orderNo
     */
    void useUserCoupon(String couponCode, String orderNo);

    void useUserCoupon2(String couponCode, String orderNo);

    Long getUserCouponsCount(Map<String, Object> query);
}
