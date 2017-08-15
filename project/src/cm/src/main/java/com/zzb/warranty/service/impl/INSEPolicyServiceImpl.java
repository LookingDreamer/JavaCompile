package com.zzb.warranty.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.warranty.dao.INSEPolicyDao;
import com.zzb.warranty.model.INSEPolicy;
import com.zzb.warranty.service.INSEPolicyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/11.
 */
@Service
public class INSEPolicyServiceImpl extends BaseServiceImpl<INSEPolicy> implements INSEPolicyService {
    @Resource
    INSEPolicyDao policyDao;


    @Override
    protected BaseDao<INSEPolicy> getBaseDao() {
        return policyDao;
    }

    @Override
    public INSEPolicy selectPolicyByOrderId(String orderId) {
        INSEPolicy insePolicy = new INSEPolicy();
        insePolicy.setOrderid(orderId);

        return policyDao.selectOne(insePolicy);
    }
}
