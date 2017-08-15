package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHInsuredhisDao;
import com.zzb.cm.entity.INSHInsuredhis;
import com.zzb.cm.service.INSHInsuredhisService;

@Service
@Transactional
public class INSHInsuredhisServiceImpl extends BaseServiceImpl<INSHInsuredhis> implements
		INSHInsuredhisService {
	@Resource
	private INSHInsuredhisDao inshInsuredhisDao;

	@Override
	protected BaseDao<INSHInsuredhis> getBaseDao() {
		return inshInsuredhisDao;
	}

}