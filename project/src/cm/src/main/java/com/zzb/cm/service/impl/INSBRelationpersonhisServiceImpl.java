package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.service.INSBRelationpersonhisService;

@Service
@Transactional
public class INSBRelationpersonhisServiceImpl extends BaseServiceImpl<INSBRelationpersonhis> implements
		INSBRelationpersonhisService {
	@Resource
	private INSBRelationpersonhisDao insbRelationpersonhisDao;

	@Override
	protected BaseDao<INSBRelationpersonhis> getBaseDao() {
		return insbRelationpersonhisDao;
	}

}