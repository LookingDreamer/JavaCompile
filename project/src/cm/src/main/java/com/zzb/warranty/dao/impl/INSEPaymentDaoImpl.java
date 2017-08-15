package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.INSEPaymentDao;
import com.zzb.warranty.model.INSEPayment;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/14.
 */
@Repository
public class INSEPaymentDaoImpl extends BaseDaoImpl<INSEPayment> implements INSEPaymentDao {
    @Override
    public INSEPayment getPaymentByTransactionId(String transactionId) {

        if (StringUtils.isBlank(transactionId)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
//        params.put("orderNo", orderNo);
        params.put("transactionId", transactionId);
        return sqlSessionTemplate.selectOne(this.getSqlName("selectOne"), params);
    }
}
