package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.service.INSBCarinfohisService;

@Service
@Transactional
public class INSBCarinfohisServiceImpl extends BaseServiceImpl<INSBCarinfohis> implements
		INSBCarinfohisService {
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;

	@Override
	protected BaseDao<INSBCarinfohis> getBaseDao() {
		return insbCarinfohisDao;
	}

}