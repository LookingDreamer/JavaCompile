package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBFlowinfoDao;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.service.INSBFlowinfoService;

@Service
@Transactional
public class INSBFlowinfoServiceImpl extends BaseServiceImpl<INSBFlowinfo> implements
		INSBFlowinfoService {
	@Resource
	private INSBFlowinfoDao insbFlowinfoDao;

	@Override
	protected BaseDao<INSBFlowinfo> getBaseDao() {
		return insbFlowinfoDao;
	}

}