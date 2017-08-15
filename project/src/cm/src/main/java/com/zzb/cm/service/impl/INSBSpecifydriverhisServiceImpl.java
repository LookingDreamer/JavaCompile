package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBSpecifydriverhisDao;
import com.zzb.cm.entity.INSBSpecifydriverhis;
import com.zzb.cm.service.INSBSpecifydriverhisService;

@Service
@Transactional
public class INSBSpecifydriverhisServiceImpl extends BaseServiceImpl<INSBSpecifydriverhis> implements
		INSBSpecifydriverhisService {
	@Resource
	private INSBSpecifydriverhisDao insbSpecifydriverhisDao;

	@Override
	protected BaseDao<INSBSpecifydriverhis> getBaseDao() {
		return insbSpecifydriverhisDao;
	}

}