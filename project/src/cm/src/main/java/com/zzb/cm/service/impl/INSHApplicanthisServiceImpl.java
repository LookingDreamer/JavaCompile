package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHApplicanthisDao;
import com.zzb.cm.entity.INSHApplicanthis;
import com.zzb.cm.service.INSHApplicanthisService;

@Service
@Transactional
public class INSHApplicanthisServiceImpl extends BaseServiceImpl<INSHApplicanthis> implements
		INSHApplicanthisService {
	@Resource
	private INSHApplicanthisDao inshApplicanthisDao;

	@Override
	protected BaseDao<INSHApplicanthis> getBaseDao() {
		return inshApplicanthisDao;
	}

}