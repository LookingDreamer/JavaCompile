package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHSpecifydriverhisDao;
import com.zzb.cm.entity.INSHSpecifydriverhis;
import com.zzb.cm.service.INSHSpecifydriverhisService;

@Service
@Transactional
public class INSHSpecifydriverhisServiceImpl extends BaseServiceImpl<INSHSpecifydriverhis> implements
		INSHSpecifydriverhisService {
	@Resource
	private INSHSpecifydriverhisDao inshSpecifydriverhisDao;

	@Override
	protected BaseDao<INSHSpecifydriverhis> getBaseDao() {
		return inshSpecifydriverhisDao;
	}

}