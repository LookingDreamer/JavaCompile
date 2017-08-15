package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.CouponDao;
import com.zzb.warranty.model.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 */
@Repository
public class CouponDaoImpl extends BaseDaoImpl<Coupon> implements CouponDao {
    @Override
    public Coupon getCouponById(String couponId) {
        return this.sqlSessionTemplate.selectOne(getSqlName("getCouponById"), couponId);
    }

    @Override
    public List<Coupon> getCoupons() {
        return this.sqlSessionTemplate.selectList(getSqlName("getCoupons"));
    }

    @Override
    public void insertCoupon(Coupon coupon) {
        this.sqlSessionTemplate.insert(getSqlName("insertCoupon"), coupon);
    }

    @Override
    public void updateCoupon(Coupon coupon) {
        this.sqlSessionTemplate.update(getSqlName("updateCoupon"), coupon);
    }
}
