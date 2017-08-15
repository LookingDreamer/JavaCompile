package com.zzb.conf.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRenewalconfigitem;

public interface INSBRenewalconfigitemDao extends BaseDao<INSBRenewalconfigitem> {

    public int deleteByAgreementid(String agreementid);

}