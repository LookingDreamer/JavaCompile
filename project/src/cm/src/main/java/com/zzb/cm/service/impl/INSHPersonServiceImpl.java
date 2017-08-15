package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHPersonDao;
import com.zzb.cm.entity.INSHPerson;
import com.zzb.cm.service.INSHPersonService;

@Service
@Transactional
public class INSHPersonServiceImpl extends BaseServiceImpl<INSHPerson> implements
		INSHPersonService {
	@Resource
	private INSHPersonDao inshPersonDao;

	@Override
	protected BaseDao<INSHPerson> getBaseDao() {
		return inshPersonDao;
	}

}