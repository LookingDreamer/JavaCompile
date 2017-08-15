package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHOrderdeliveryDao;
import com.zzb.cm.entity.INSHOrderdelivery;
import com.zzb.cm.service.INSHOrderdeliveryService;

@Service
@Transactional
public class INSHOrderdeliveryServiceImpl extends BaseServiceImpl<INSHOrderdelivery> implements
		INSHOrderdeliveryService {
	@Resource
	private INSHOrderdeliveryDao inshOrderdeliveryDao;

	@Override
	protected BaseDao<INSHOrderdelivery> getBaseDao() {
		return inshOrderdeliveryDao;
	}

}