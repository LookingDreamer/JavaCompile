package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBInterfaceDao;
import com.zzb.conf.entity.INSBInterface;
import com.zzb.conf.service.INSBInterfaceService;

@Service
@Transactional
public class INSBInterfaceServiceImpl extends BaseServiceImpl<INSBInterface> implements
		INSBInterfaceService {
	@Resource
	private INSBInterfaceDao insbInterfaceDao;

	@Override
	protected BaseDao<INSBInterface> getBaseDao() {
		return insbInterfaceDao;
	}

}