package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBCorecodemapDao;
import com.zzb.cm.entity.INSBCorecodemap;
import com.zzb.cm.service.INSBCorecodemapService;

@Service
@Transactional
public class INSBCorecodemapServiceImpl extends BaseServiceImpl<INSBCorecodemap> implements
		INSBCorecodemapService {
	@Resource
	private INSBCorecodemapDao insbCorecodemapDao;

	@Override
	protected BaseDao<INSBCorecodemap> getBaseDao() {
		return insbCorecodemapDao;
	}

}