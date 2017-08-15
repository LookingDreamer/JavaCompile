package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRiskplanrelationDao;
import com.zzb.conf.entity.INSBRiskplanrelation;
import com.zzb.conf.service.INSBRiskplanrelationService;

@Service
@Transactional
public class INSBRiskplanrelationServiceImpl extends BaseServiceImpl<INSBRiskplanrelation> implements
		INSBRiskplanrelationService {
	@Resource
	private INSBRiskplanrelationDao insbRiskplanrelationDao;

	@Override
	protected BaseDao<INSBRiskplanrelation> getBaseDao() {
		return insbRiskplanrelationDao;
	}

}