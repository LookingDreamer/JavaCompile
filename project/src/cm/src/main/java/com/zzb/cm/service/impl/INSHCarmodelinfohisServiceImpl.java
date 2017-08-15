package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHCarmodelinfohisDao;
import com.zzb.cm.entity.INSHCarmodelinfohis;
import com.zzb.cm.service.INSHCarmodelinfohisService;

@Service
@Transactional
public class INSHCarmodelinfohisServiceImpl extends BaseServiceImpl<INSHCarmodelinfohis> implements
		INSHCarmodelinfohisService {
	@Resource
	private INSHCarmodelinfohisDao inshCarmodelinfohisDao;

	@Override
	protected BaseDao<INSHCarmodelinfohis> getBaseDao() {
		return inshCarmodelinfohisDao;
	}

}