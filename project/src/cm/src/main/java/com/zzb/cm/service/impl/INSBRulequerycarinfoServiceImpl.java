package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.cm.entity.INSBRulequerycarinfo;
import com.zzb.cm.service.INSBRulequerycarinfoService;

@Service
@Transactional
public class INSBRulequerycarinfoServiceImpl extends BaseServiceImpl<INSBRulequerycarinfo> implements
		INSBRulequerycarinfoService {
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;

	@Override
	protected BaseDao<INSBRulequerycarinfo> getBaseDao() {
		return insbRulequerycarinfoDao;
	}

}