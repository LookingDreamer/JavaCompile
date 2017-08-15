package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBDistributiontype;

public interface INSBDistributiontypeDao extends BaseDao<INSBDistributiontype> {

	public Long deleteByExceptIds(List<String> ids, String agreementid);

    public Long deleteByAgreementId(String agreementid);

}