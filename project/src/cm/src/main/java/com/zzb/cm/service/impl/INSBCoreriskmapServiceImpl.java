package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBCoreriskmapDao;
import com.zzb.cm.entity.INSBCoreriskmap;
import com.zzb.cm.service.INSBCoreriskmapService;

@Service
@Transactional
public class INSBCoreriskmapServiceImpl extends BaseServiceImpl<INSBCoreriskmap> implements
		INSBCoreriskmapService {
	@Resource
	private INSBCoreriskmapDao insbCoreriskmapDao;

	@Override
	protected BaseDao<INSBCoreriskmap> getBaseDao() {
		return insbCoreriskmapDao;
	}

}