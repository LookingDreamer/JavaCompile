package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBAgreementareaDao;
import com.zzb.conf.entity.INSBAgreementarea;
import com.zzb.conf.service.INSBAgreementareaService;

@Service
@Transactional
public class INSBAgreementareaServiceImpl extends BaseServiceImpl<INSBAgreementarea> implements
		INSBAgreementareaService {
	@Resource 
	private INSBAgreementareaDao insbAgreementareaDao;

	@Override
	protected BaseDao<INSBAgreementarea> getBaseDao() {
		return insbAgreementareaDao;
	}

}