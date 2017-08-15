package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBGrouptasksetDao;
import com.zzb.conf.entity.INSBGrouptaskset;
import com.zzb.conf.service.INSBGrouptasksetService;

@Service
@Transactional
public class INSBGrouptasksetServiceImpl extends BaseServiceImpl<INSBGrouptaskset> implements
		INSBGrouptasksetService {
	@Resource
	private INSBGrouptasksetDao insbGrouptasksetDao;

	@Override
	protected BaseDao<INSBGrouptaskset> getBaseDao() {
		return insbGrouptasksetDao;
	}

}