package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.CouponConfig;

/**
 * Created by Administrator on 2017/2/8.
 */
public interface CouponConfigDao extends BaseDao<CouponConfig> {
    CouponConfig get(String key);

    void update(CouponConfig couponConfig);

    void insert(CouponConfig couponConfig);

    void remove(String key);
}
