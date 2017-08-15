package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBOrderexceptionsDao;
import com.zzb.conf.entity.INSBOrderexceptions;
import com.zzb.conf.service.INSBOrderexceptionsService;

@Service
@Transactional
public class INSBOrderexceptionsServiceImpl extends BaseServiceImpl<INSBOrderexceptions> implements
		INSBOrderexceptionsService {
	@Resource
	private INSBOrderexceptionsDao insbOrderexceptionsDao;

	@Override
	protected BaseDao<INSBOrderexceptions> getBaseDao() {
		return insbOrderexceptionsDao;
	}

}