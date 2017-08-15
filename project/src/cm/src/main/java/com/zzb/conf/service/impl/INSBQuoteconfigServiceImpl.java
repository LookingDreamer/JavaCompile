package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBQuoteconfigDao;
import com.zzb.conf.entity.INSBQuoteconfig;
import com.zzb.conf.service.INSBQuoteconfigService;

@Service
@Transactional
public class INSBQuoteconfigServiceImpl extends BaseServiceImpl<INSBQuoteconfig> implements
		INSBQuoteconfigService {
	@Resource
	private INSBQuoteconfigDao insbQuoteconfigDao;

	@Override
	protected BaseDao<INSBQuoteconfig> getBaseDao() {
		return insbQuoteconfigDao;
	}

}