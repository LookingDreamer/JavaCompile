package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBDistributiontypeDao;
import com.zzb.conf.entity.INSBDistributiontype;
import com.zzb.conf.service.INSBDistributiontypeService;

@Service
@Transactional
public class INSBDistributiontypeServiceImpl extends BaseServiceImpl<INSBDistributiontype> implements
		INSBDistributiontypeService {
	@Resource
	private INSBDistributiontypeDao insbDistributiontypeDao;

	@Override
	protected BaseDao<INSBDistributiontype> getBaseDao() {
		return insbDistributiontypeDao;
	}

	@Override
	public Long deleteByExceptIds(List<String> ids, String agreementid) {
		return insbDistributiontypeDao.deleteByExceptIds(ids,agreementid);
	}

    public Long deleteByAgreementId(String agreementid) {
        return insbDistributiontypeDao.deleteByAgreementId(agreementid);
    }
}