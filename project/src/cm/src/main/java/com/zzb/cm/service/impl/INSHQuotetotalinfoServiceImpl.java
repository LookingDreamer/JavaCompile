package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHQuotetotalinfoDao;
import com.zzb.cm.entity.INSHQuotetotalinfo;
import com.zzb.cm.service.INSHQuotetotalinfoService;

@Service
@Transactional
public class INSHQuotetotalinfoServiceImpl extends BaseServiceImpl<INSHQuotetotalinfo> implements
		INSHQuotetotalinfoService {
	@Resource
	private INSHQuotetotalinfoDao inshQuotetotalinfoDao;

	@Override
	protected BaseDao<INSHQuotetotalinfo> getBaseDao() {
		return inshQuotetotalinfoDao;
	}

}