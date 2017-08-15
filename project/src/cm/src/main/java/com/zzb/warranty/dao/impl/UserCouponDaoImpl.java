package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.UserCouponDao;
import com.zzb.warranty.model.UserCoupon;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Administrator on 2017/2/9.
 */
@Repository
public class UserCouponDaoImpl extends BaseDaoImpl<UserCoupon> implements UserCouponDao {
    @Override
    public UserCoupon getUserCouponByCouponCode(String couponCode) {
        return sqlSessionTemplate.selectOne(getSqlName("getUserCouponByCouponCode"), couponCode);
    }

    @Override
    public UserCoupon getUserCoupon(String couponCode, String userCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        params.put("userCode", userCode);
        return sqlSessionTemplate.selectOne(getSqlName("getUserCoupon"), params);
    }

    @Override
    public void insertUserCoupon(UserCoupon userCoupon) {
        sqlSessionTemplate.insert(getSqlName("insertUserCoupon"), userCoupon);
    }

    @Override
    public void removeUserCoupon(String couponCode) {
        sqlSessionTemplate.delete(getSqlName("removeUserCoupon"), couponCode);
    }

    @Override
    public List<UserCoupon> getUserCoupons(String userCode) {
        if (userCode == null) {
            return Collections.emptyList();
        }
        return sqlSessionTemplate.selectList(getSqlName("getUserCoupons"), userCode);
    }

    @Override
    public List<UserCoupon> getUserCouponsByProduct(String userCode, String productCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("userCode", userCode);
        params.put("productCode", productCode);
        return sqlSessionTemplate.selectList(getSqlName("getUserCouponsByProduct"), params);
    }

    @Override
    public List<UserCoupon> getUserCoupons(Map<String, Object> query) {

        return sqlSessionTemplate.selectList(getSqlName("getUserCouponsPageable"), query);
    }

    @Override
    public void updateUserCoupon(UserCoupon userCoupon) {
        sqlSessionTemplate.update(getSqlName("updateUserCoupon"), userCoupon);
    }

    @Override
    public void useUserCoupon(String couponCode, String orderNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        params.put("orderNo", orderNo);
        params.put("usedTime", new Date());
        sqlSessionTemplate.update(getSqlName("useUserCoupon"), params);
    }

    @Override
    public void useUserCoupon2(String couponCode, String orderNo) {
        Map<String, String> params = new HashMap<>();
        params.put("couponCode", couponCode);
        params.put("orderNo", orderNo);
        sqlSessionTemplate.update(getSqlName("useUserCoupon"), params);
    }

    @Override
    public Long getUserCouponsCount(Map<String, Object> query) {
        return sqlSessionTemplate.selectOne(getSqlName("getUserCouponsCount"), query);
    }
}
