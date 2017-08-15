package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBMinimsglogDao;
import com.zzb.extra.entity.INSBMinimsglog;
import com.zzb.extra.service.INSBMinimsglogService;

@Service
@Transactional
public class INSBMinimsglogServiceImpl extends BaseServiceImpl<INSBMinimsglog> implements
		INSBMinimsglogService {
	@Resource
	private INSBMinimsglogDao insbMinimsglogDao;

	@Override
	protected BaseDao<INSBMinimsglog> getBaseDao() {
		return insbMinimsglogDao;
	}

}