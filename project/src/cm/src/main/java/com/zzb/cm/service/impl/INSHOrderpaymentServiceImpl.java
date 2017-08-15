package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHOrderpaymentDao;
import com.zzb.cm.entity.INSHOrderpayment;
import com.zzb.cm.service.INSHOrderpaymentService;

@Service
@Transactional
public class INSHOrderpaymentServiceImpl extends BaseServiceImpl<INSHOrderpayment> implements
		INSHOrderpaymentService {
	@Resource
	private INSHOrderpaymentDao inshOrderpaymentDao;

	@Override
	protected BaseDao<INSHOrderpayment> getBaseDao() {
		return inshOrderpaymentDao;
	}

}