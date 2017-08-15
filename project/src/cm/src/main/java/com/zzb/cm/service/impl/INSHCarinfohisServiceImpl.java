package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHCarinfohisDao;
import com.zzb.cm.entity.INSHCarinfohis;
import com.zzb.cm.service.INSHCarinfohisService;

@Service
@Transactional
public class INSHCarinfohisServiceImpl extends BaseServiceImpl<INSHCarinfohis> implements
		INSHCarinfohisService {
	@Resource
	private INSHCarinfohisDao inshCarinfohisDao;

	@Override
	protected BaseDao<INSHCarinfohis> getBaseDao() {
		return inshCarinfohisDao;
	}

}