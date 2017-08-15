package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.service.INSBApplicanthisService;

@Service
@Transactional
public class INSBApplicanthisServiceImpl extends BaseServiceImpl<INSBApplicanthis> implements
		INSBApplicanthisService {
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;

	@Override
	protected BaseDao<INSBApplicanthis> getBaseDao() {
		return insbApplicanthisDao;
	}

}