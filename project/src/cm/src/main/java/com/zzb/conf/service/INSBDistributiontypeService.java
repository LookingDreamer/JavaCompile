package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBDistributiontype;

public interface INSBDistributiontypeService extends BaseService<INSBDistributiontype> {

	public Long deleteByExceptIds(List<String> ids, String agreementid);

    public Long deleteByAgreementId(String agreementid);

}