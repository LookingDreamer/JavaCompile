package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.service.INSBFilebusinessService;

@Service
@Transactional
public class INSBFilebusinessServiceImpl extends BaseServiceImpl<INSBFilebusiness> implements
		INSBFilebusinessService {
	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;

	@Override
	protected BaseDao<INSBFilebusiness> getBaseDao() {
		return insbFilebusinessDao;
	}

}