package com.zzb.warranty.service.async;

import com.zzb.warranty.dao.UserCouponDao;
import com.zzb.warranty.model.UserCoupon;
import com.zzb.warranty.service.UserCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by patri on 2017/2/19.
 */
@Service
public class AsyncUserCouponServiceImpl implements UserCouponService {
    AsyncTaskHelper asyncTaskHelper;
    @Resource
    UserCouponDao userCouponDao;

    public AsyncUserCouponServiceImpl() {
        this.asyncTaskHelper = AsyncTaskHelper.getInstance();
    }

    @Override
    public void insertUserCoupon(UserCoupon userCoupon) {
        asyncTaskHelper.asyncInsert(userCoupon, userCouponDao);
    }

    @Override
    public UserCoupon getUserCouponByCouponCode(String couponCode) {
        //nothing
        return null;
    }

    @Override
    public List<UserCoupon> getUserCouponsByUserCode(String userId) {
        //nothing
        return null;
    }

    @Override
    public UserCoupon getUserCoupon(String couponCode, String userCode) {
        //nothing
        return null;
    }

    @Override
    public void useUserCoupon(String couponCode, String userCode, String orderNo, String productCode) {
        //nothing
    }

    @Override
    public List<UserCoupon> getUserCouponsPageable(Map<String, Object> query) {
        //nothing
        return null;
    }

    @Override
    public Long getUserCouponCount(Map<String, Object> query) {
        //nothing
        return null;
    }
}
