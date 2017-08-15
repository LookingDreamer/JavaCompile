package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.warranty.model.INSEPolicy;

/**
 * Created by Administrator on 2017/1/11.
 */
public interface INSEPolicyService extends BaseService<INSEPolicy> {

    INSEPolicy selectPolicyByOrderId(String orderId);
}
