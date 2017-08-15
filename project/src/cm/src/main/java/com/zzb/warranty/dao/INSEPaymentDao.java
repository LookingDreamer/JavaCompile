package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.INSEPayment;

/**
 * Created by Administrator on 2017/1/14.
 */
public interface INSEPaymentDao extends BaseDao<INSEPayment> {

    INSEPayment getPaymentByTransactionId(String transactionId);


}
