package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBLastyearinsurestatusDao;
import com.zzb.cm.entity.INSBLastyearinsurestatus;
import com.zzb.cm.service.INSBLastyearinsurestatusService;

@Service
@Transactional
public class INSBLastyearinsurestatusServiceImpl extends BaseServiceImpl<INSBLastyearinsurestatus> implements
		INSBLastyearinsurestatusService {
	@Resource
	private INSBLastyearinsurestatusDao insbLastyearinsurestatusDao;

	@Override
	protected BaseDao<INSBLastyearinsurestatus> getBaseDao() {
		return insbLastyearinsurestatusDao;
	}

}