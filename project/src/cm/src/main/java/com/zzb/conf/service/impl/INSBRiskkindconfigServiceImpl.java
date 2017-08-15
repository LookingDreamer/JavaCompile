package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.service.INSBRiskkindconfigService;

@Service
@Transactional
public class INSBRiskkindconfigServiceImpl extends BaseServiceImpl<INSBRiskkindconfig> implements
		INSBRiskkindconfigService {
	@Resource
	private INSBRiskkindconfigDao insbRiskkindconfigDao;

	@Override
	protected BaseDao<INSBRiskkindconfig> getBaseDao() {
		return insbRiskkindconfigDao;
	}

	@Override
	public INSBRiskkindconfig selectKindByKindcode(String riskkindcode) {
		INSBRiskkindconfig insbRiskkindconfig = insbRiskkindconfigDao.selectKindByKindcode(riskkindcode);
		return insbRiskkindconfig;
	}

	@Override
	public List<INSBRiskkindconfig> queryAll() {
		return insbRiskkindconfigDao.selectAll();
	}

}