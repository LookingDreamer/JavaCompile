package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBCarconfigDao;
import com.zzb.cm.entity.INSBCarconfig;
import com.zzb.cm.service.INSBCarconfigService;

@Service
@Transactional
public class INSBCarconfigServiceImpl extends BaseServiceImpl<INSBCarconfig> implements
		INSBCarconfigService {
	@Resource
	private INSBCarconfigDao insbCarconfigDao;

	@Override
	protected BaseDao<INSBCarconfig> getBaseDao() {
		return insbCarconfigDao;
	}

}