package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.service.INSBApplicantService;

@Service
@Transactional
public class INSBApplicantServiceImpl extends BaseServiceImpl<INSBApplicant> implements
		INSBApplicantService {
	@Resource
	private INSBApplicantDao insbApplicantDao;

	@Override
	protected BaseDao<INSBApplicant> getBaseDao() {
		return insbApplicantDao;
	}

}