package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.UserCouponTimesDao;
import com.zzb.warranty.model.CouponTimes;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/2/9.
 */
@Repository
public class UserCouponTimesDaoImpl extends BaseDaoImpl<CouponTimes> implements UserCouponTimesDao {
    @Override
    public CouponTimes getCouponTimes(String userCode) {
        return sqlSessionTemplate.selectOne(getSqlName("getCouponTimes"), userCode);
    }

    @Override
    public void updateCouponTimes(CouponTimes couponTimes) {
        sqlSessionTemplate.update(getSqlName("updateCouponTimes"), couponTimes);
    }

    @Override
    public void insertCouponTimes(CouponTimes couponTimes) {
        sqlSessionTemplate.insert(getSqlName("insertCouponTimes"), couponTimes);
    }
}
