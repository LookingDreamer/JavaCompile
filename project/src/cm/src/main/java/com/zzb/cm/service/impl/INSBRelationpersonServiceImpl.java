package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.service.INSBRelationpersonService;

@Service
@Transactional
public class INSBRelationpersonServiceImpl extends BaseServiceImpl<INSBRelationperson> implements
		INSBRelationpersonService {
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;

	@Override
	protected BaseDao<INSBRelationperson> getBaseDao() {
		return insbRelationpersonDao;
	}

}