package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBGroupprovideDao;
import com.zzb.conf.entity.INSBGroupprovide;
import com.zzb.conf.service.INSBGroupprovideService;

@Service
@Transactional
public class INSBGroupprovideServiceImpl extends BaseServiceImpl<INSBGroupprovide> implements
		INSBGroupprovideService {
	@Resource
	private INSBGroupprovideDao insbGroupprovideDao;

	@Override
	protected BaseDao<INSBGroupprovide> getBaseDao() {
		return insbGroupprovideDao;
	}

}