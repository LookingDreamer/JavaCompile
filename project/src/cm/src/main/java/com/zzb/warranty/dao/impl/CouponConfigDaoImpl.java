package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.CouponConfigDao;
import com.zzb.warranty.model.CouponConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/2/9.
 */
@Repository
public class CouponConfigDaoImpl extends BaseDaoImpl<CouponConfig> implements CouponConfigDao {

    @Override
    public CouponConfig get(String key) {
        return this.sqlSessionTemplate.selectOne(getSqlName("get"), key);
    }

    @Override
    public void update(CouponConfig couponConfig) {
        sqlSessionTemplate.update(getSqlName("update"), couponConfig);
    }

    @Override
    public void remove(String key) {
        sqlSessionTemplate.delete(getSqlName("delete"), key);
    }
}
