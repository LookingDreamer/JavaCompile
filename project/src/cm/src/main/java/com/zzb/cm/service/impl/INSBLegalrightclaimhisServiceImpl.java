package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.service.INSBLegalrightclaimhisService;

@Service
@Transactional
public class INSBLegalrightclaimhisServiceImpl extends BaseServiceImpl<INSBLegalrightclaimhis> implements
		INSBLegalrightclaimhisService {
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;

	@Override
	protected BaseDao<INSBLegalrightclaimhis> getBaseDao() {
		return insbLegalrightclaimhisDao;
	}

}