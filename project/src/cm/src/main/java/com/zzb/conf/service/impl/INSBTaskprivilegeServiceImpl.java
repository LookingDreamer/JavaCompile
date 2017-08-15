package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBTaskprivilegeDao;
import com.zzb.conf.entity.INSBTaskprivilege;
import com.zzb.conf.service.INSBTaskprivilegeService;

@Service
@Transactional
public class INSBTaskprivilegeServiceImpl extends BaseServiceImpl<INSBTaskprivilege> implements
		INSBTaskprivilegeService {
	@Resource
	private INSBTaskprivilegeDao insbTaskprivilegeDao;

	@Override
	protected BaseDao<INSBTaskprivilege> getBaseDao() {
		return insbTaskprivilegeDao;
	}

}