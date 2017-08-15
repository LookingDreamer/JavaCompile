package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHPolicyitemDao;
import com.zzb.cm.entity.INSHPolicyitem;
import com.zzb.cm.service.INSHPolicyitemService;

@Service
@Transactional
public class INSHPolicyitemServiceImpl extends BaseServiceImpl<INSHPolicyitem> implements
		INSHPolicyitemService {
	@Resource
	private INSHPolicyitemDao inshPolicyitemDao;

	@Override
	protected BaseDao<INSHPolicyitem> getBaseDao() {
		return inshPolicyitemDao;
	}

}