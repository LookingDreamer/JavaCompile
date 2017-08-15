package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBItemprovidestatusDao;
import com.zzb.conf.entity.INSBItemprovidestatus;
import com.zzb.conf.service.INSBItemprovidestatusService;

@Service
@Transactional
public class INSBItemprovidestatusServiceImpl extends BaseServiceImpl<INSBItemprovidestatus> implements
		INSBItemprovidestatusService {
	@Resource
	private INSBItemprovidestatusDao insbItemprovidestatusDao;

	@Override
	protected BaseDao<INSBItemprovidestatus> getBaseDao() {
		return insbItemprovidestatusDao;
	}

}