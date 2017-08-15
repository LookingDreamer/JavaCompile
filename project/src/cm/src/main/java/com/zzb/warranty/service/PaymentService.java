package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.model.INSEPayment;

/**
 * Created by Administrator on 2017/1/14.
 */
public interface PaymentService extends BaseService<INSEPayment>{
    String pay(INSBAgent agent, String orderno, String paychannelid, String paytype, String paysource, String redirectUrl, String callbackUrl);
    INSEPayment getPaymentByTransactionId(String transactionId);

    void updatePayment(INSEPayment payment);
}
