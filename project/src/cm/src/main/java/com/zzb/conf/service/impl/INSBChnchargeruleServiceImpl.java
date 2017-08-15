package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBChnchargeruleDao;
import com.zzb.conf.entity.INSBChnchargerule;
import com.zzb.conf.service.INSBChnchargeruleService;

@Service
@Transactional
public class INSBChnchargeruleServiceImpl extends BaseServiceImpl<INSBChnchargerule> implements
		INSBChnchargeruleService {
	@Resource
	private INSBChnchargeruleDao insbChnchargeruleDao;
 
	@Override
	protected BaseDao<INSBChnchargerule> getBaseDao() {
		return insbChnchargeruleDao;
	}

}