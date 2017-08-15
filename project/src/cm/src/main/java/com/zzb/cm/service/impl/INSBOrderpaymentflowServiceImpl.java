package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBOrderpaymentflowDao;
import com.zzb.cm.entity.INSBOrderpaymentflow;
import com.zzb.cm.service.INSBOrderpaymentflowService;

@Service
@Transactional
public class INSBOrderpaymentflowServiceImpl extends BaseServiceImpl<INSBOrderpaymentflow> implements
		INSBOrderpaymentflowService {
	@Resource
	private INSBOrderpaymentflowDao insbOrderpaymentflowDao;

	@Override
	protected BaseDao<INSBOrderpaymentflow> getBaseDao() {
		return insbOrderpaymentflowDao;
	}

}