package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBTasksetrulebseDao;
import com.zzb.conf.entity.INSBTasksetrulebse;
import com.zzb.conf.service.INSBTasksetrulebseService;

@Service
@Transactional
public class INSBTasksetrulebseServiceImpl extends
		BaseServiceImpl<INSBTasksetrulebse> implements
		INSBTasksetrulebseService {
	@Resource
	private INSBTasksetrulebseDao insbTasksetrulebseDao;

	@Override
	protected BaseDao<INSBTasksetrulebse> getBaseDao() {
		return insbTasksetrulebseDao;
	}


}