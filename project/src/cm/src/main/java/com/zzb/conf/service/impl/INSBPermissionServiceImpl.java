package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBPermissionDao;
import com.zzb.conf.entity.INSBPermission;
import com.zzb.conf.service.INSBPermissionService;
 
@Service
@Transactional
public class INSBPermissionServiceImpl extends BaseServiceImpl<INSBPermission> implements
		INSBPermissionService {
	@Resource
	private INSBPermissionDao insbPermissionDao;

	@Override
	protected BaseDao<INSBPermission> getBaseDao() {
		return insbPermissionDao;
	}

}