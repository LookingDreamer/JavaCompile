package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHLegalrightclaimhisDao;
import com.zzb.cm.entity.INSHLegalrightclaimhis;
import com.zzb.cm.service.INSHLegalrightclaimhisService;

@Service
@Transactional
public class INSHLegalrightclaimhisServiceImpl extends BaseServiceImpl<INSHLegalrightclaimhis> implements
		INSHLegalrightclaimhisService {
	@Resource
	private INSHLegalrightclaimhisDao inshLegalrightclaimhisDao;

	@Override
	protected BaseDao<INSHLegalrightclaimhis> getBaseDao() {
		return inshLegalrightclaimhisDao;
	}

}