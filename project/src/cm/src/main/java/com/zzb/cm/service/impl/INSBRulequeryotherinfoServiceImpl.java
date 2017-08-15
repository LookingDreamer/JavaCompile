package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRulequeryotherinfoDao;
import com.zzb.cm.entity.INSBRulequeryotherinfo;
import com.zzb.cm.service.INSBRulequeryotherinfoService;

@Service
@Transactional
public class INSBRulequeryotherinfoServiceImpl extends BaseServiceImpl<INSBRulequeryotherinfo> implements
		INSBRulequeryotherinfoService {
	@Resource
	private INSBRulequeryotherinfoDao insbRulequeryotherinfoDao;

	@Override
	protected BaseDao<INSBRulequeryotherinfo> getBaseDao() {
		return insbRulequeryotherinfoDao;
	}

}