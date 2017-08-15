package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.service.INSBLegalrightclaimService;

@Service
@Transactional
public class INSBLegalrightclaimServiceImpl extends BaseServiceImpl<INSBLegalrightclaim> implements
		INSBLegalrightclaimService {
	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;

	@Override
	protected BaseDao<INSBLegalrightclaim> getBaseDao() {
		return insbLegalrightclaimDao;
	}

}