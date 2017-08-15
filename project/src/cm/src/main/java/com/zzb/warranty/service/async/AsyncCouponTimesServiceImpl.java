package com.zzb.warranty.service.async;

import com.zzb.warranty.dao.UserCouponTimesDao;
import com.zzb.warranty.model.CouponTimes;
import com.zzb.warranty.service.impl.CouponTimesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/2/20.
 */
@Service
public class AsyncCouponTimesServiceImpl implements CouponTimesService {
    private AsyncTaskHelper asyncTaskHelper = AsyncTaskHelper.getInstance();
    @Resource
    UserCouponTimesDao userCouponTimesDao;

    @Override
    public CouponTimes getCouponTimes(String userCode) {
        return null;
    }

    @Override
    public void updateCouponTimes(CouponTimes couponTimes) {
        asyncTaskHelper.asyncUpdate(couponTimes, userCouponTimesDao);
    }

    @Override
    public void insertCouponTimes(CouponTimes couponTimes) {
        asyncTaskHelper.asyncInsert(couponTimes, userCouponTimesDao);
    }
}
