package com.zzb.warranty.service.impl;

import com.zzb.warranty.dao.UserCouponDao;
import com.zzb.warranty.model.UserCoupon;
import com.zzb.warranty.service.UserCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
@Service
public class UserCouponServiceImpl implements UserCouponService {
    @Resource
    UserCouponDao userCouponDao;

    @Override
    public void insertUserCoupon(UserCoupon userCoupon) {

    }

    @Override
    public UserCoupon getUserCouponByCouponCode(String couponCode) {
        return null;
    }

    @Override
    public List<UserCoupon> getUserCouponsByUserCode(String userId) {
        return null;
    }

    @Override
    public UserCoupon getUserCoupon(String couponCode, String userCode) {
        return null;
    }

    @Override
    public void useUserCoupon(String couponCode, String userCode, String orderNo, String productCode) {
        // 检测usercoupon是否存在
        UserCoupon userCoupon = getUserCoupon(couponCode, userCode);
        if (userCoupon == null) {
            throw new RuntimeException("奖券不存在");
        }

        // 是否已使用
        if (userCoupon.isUsed()) {
            throw new RuntimeException("奖券已使用");
        }

        //有效性，是否在有效期内
        Date effectiveTime = userCoupon.getEffectiveTime();
        Date expireTime = userCoupon.getExpireTime();
        Date currentTime = new Date();
        if ((!currentTime.after(effectiveTime)) || (!currentTime.before(expireTime))) {
            throw new RuntimeException("奖券不在使用期内");
        }

        // 是否适用产品 // TODO 此处好像应该是个列表吧，通过配置
//        String availableProduct = userCoupon.getAvailableProduct();
//        if (StringUtils.isBlank(productCode)) {
//            throw new RuntimeException("产品代码不能为空");
//        }
//
//        if (!productCode.equals(availableProduct)) {
//            throw new RuntimeException("奖券不适用于产品");
//        }

        // 更新字段orderNo, isUsed
        userCouponDao.useUserCoupon(couponCode, orderNo);
    }

    @Override
    public List<UserCoupon> getUserCouponsPageable(Map<String, Object> query) {
        return userCouponDao.getUserCoupons(query);
    }

    @Override
    public Long getUserCouponCount(Map<String, Object> query) {
        return userCouponDao.getUserCouponsCount(query);
    }
}
