package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBGroupworktimeDao;
import com.zzb.conf.entity.INSBGroupworktime;
import com.zzb.conf.service.INSBGroupworktimeService;

@Service
@Transactional
public class INSBGroupworktimeServiceImpl extends BaseServiceImpl<INSBGroupworktime> implements
		INSBGroupworktimeService {
	@Resource
	private INSBGroupworktimeDao insbGroupworktimeDao;

	@Override
	protected BaseDao<INSBGroupworktime> getBaseDao() {
		return insbGroupworktimeDao;
	}

}