package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBProviderandediDao;
import com.zzb.conf.entity.INSBProviderandedi;
import com.zzb.conf.service.INSBProviderandediService;

@Service
@Transactional
public class INSBProviderandediServiceImpl extends BaseServiceImpl<INSBProviderandedi> implements
		INSBProviderandediService {
	@Resource
	private INSBProviderandediDao insbProviderandediDao;

	@Override
	protected BaseDao<INSBProviderandedi> getBaseDao() {
		return insbProviderandediDao;
	}

	@Override
	public int addproandedi(INSBProviderandedi bean) {
		return insbProviderandediDao.addproandedi(bean);
	}

	@Override
	public int deleteRelation(String ediid) {
		return insbProviderandediDao.deleteRelation(ediid);
	}

}