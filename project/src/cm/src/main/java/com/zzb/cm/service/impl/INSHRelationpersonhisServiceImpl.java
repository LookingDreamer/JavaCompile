package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHRelationpersonhisDao;
import com.zzb.cm.entity.INSHRelationpersonhis;
import com.zzb.cm.service.INSHRelationpersonhisService;

@Service
@Transactional
public class INSHRelationpersonhisServiceImpl extends BaseServiceImpl<INSHRelationpersonhis> implements
		INSHRelationpersonhisService {
	@Resource
	private INSHRelationpersonhisDao inshRelationpersonhisDao;

	@Override
	protected BaseDao<INSHRelationpersonhis> getBaseDao() {
		return inshRelationpersonhisDao;
	}

}