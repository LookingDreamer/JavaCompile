package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHCarowneinfoDao;
import com.zzb.cm.entity.INSHCarowneinfo;
import com.zzb.cm.service.INSHCarowneinfoService;

@Service
@Transactional
public class INSHCarowneinfoServiceImpl extends BaseServiceImpl<INSHCarowneinfo> implements
		INSHCarowneinfoService {
	@Resource
	private INSHCarowneinfoDao inshCarowneinfoDao;

	@Override
	protected BaseDao<INSHCarowneinfo> getBaseDao() {
		return inshCarowneinfoDao;
	}

}