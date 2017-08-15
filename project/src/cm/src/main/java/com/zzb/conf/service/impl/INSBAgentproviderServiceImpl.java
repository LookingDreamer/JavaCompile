package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBAgentproviderDao;
import com.zzb.conf.entity.INSBAgentprovider;
import com.zzb.conf.service.INSBAgentproviderService;

@Service
@Transactional
public class INSBAgentproviderServiceImpl extends BaseServiceImpl<INSBAgentprovider> implements
		INSBAgentproviderService {
	@Resource
	private INSBAgentproviderDao insbAgentproviderDao;

	@Override
	protected BaseDao<INSBAgentprovider> getBaseDao() {
		return insbAgentproviderDao;
	}

}