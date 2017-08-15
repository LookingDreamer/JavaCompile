package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.entity.INSBAgentpermission;
import com.zzb.conf.service.INSBAgentpermissionService;

@Service
@Transactional
public class INSBAgentpermissionServiceImpl extends BaseServiceImpl<INSBAgentpermission> implements
		INSBAgentpermissionService {
	@Resource
	private INSBAgentpermissionDao insbAgentpermissionDao;

	@Override
	protected BaseDao<INSBAgentpermission> getBaseDao() {
		return insbAgentpermissionDao;
	}

}