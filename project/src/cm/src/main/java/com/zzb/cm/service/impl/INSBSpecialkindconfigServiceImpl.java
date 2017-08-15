package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.cm.service.INSBSpecialkindconfigService;

@Service
@Transactional
public class INSBSpecialkindconfigServiceImpl extends BaseServiceImpl<INSBSpecialkindconfig> implements
		INSBSpecialkindconfigService {
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;

	@Override
	protected BaseDao<INSBSpecialkindconfig> getBaseDao() {
		return insbSpecialkindconfigDao;
	}

}