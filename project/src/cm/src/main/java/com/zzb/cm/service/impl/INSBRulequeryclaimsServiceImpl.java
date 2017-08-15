package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRulequeryclaimsDao;
import com.zzb.cm.entity.INSBRulequeryclaims;
import com.zzb.cm.service.INSBRulequeryclaimsService;

@Service
@Transactional
public class INSBRulequeryclaimsServiceImpl extends BaseServiceImpl<INSBRulequeryclaims> implements
		INSBRulequeryclaimsService {
	@Resource
	private INSBRulequeryclaimsDao insbRulequeryclaimsDao;

	@Override
	protected BaseDao<INSBRulequeryclaims> getBaseDao() {
		return insbRulequeryclaimsDao;
	}

}