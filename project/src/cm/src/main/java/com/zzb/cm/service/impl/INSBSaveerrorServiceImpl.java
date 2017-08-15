package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBSaveerrorDao;
import com.zzb.cm.entity.INSBSaveerror;
import com.zzb.cm.service.INSBSaveerrorService;

@Service
@Transactional
public class INSBSaveerrorServiceImpl extends BaseServiceImpl<INSBSaveerror> implements
		INSBSaveerrorService {
	@Resource
	private INSBSaveerrorDao insbSaveerrorDao;

	@Override
	protected BaseDao<INSBSaveerror> getBaseDao() {
		return insbSaveerrorDao;
	}

}