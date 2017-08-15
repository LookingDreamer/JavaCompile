package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHOrderDao;
import com.zzb.cm.entity.INSHOrder;
import com.zzb.cm.service.INSHOrderService;

@Service
@Transactional
public class INSHOrderServiceImpl extends BaseServiceImpl<INSHOrder> implements
		INSHOrderService {
	@Resource
	private INSHOrderDao inshOrderDao;

	@Override
	protected BaseDao<INSHOrder> getBaseDao() {
		return inshOrderDao;
	}

}