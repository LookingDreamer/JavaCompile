package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSHQuoteinfoDao;
import com.zzb.cm.entity.INSHQuoteinfo;
import com.zzb.cm.service.INSHQuoteinfoService;

@Service
@Transactional
public class INSHQuoteinfoServiceImpl extends BaseServiceImpl<INSHQuoteinfo> implements
		INSHQuoteinfoService {
	@Resource
	private INSHQuoteinfoDao inshQuoteinfoDao;

	@Override
	protected BaseDao<INSHQuoteinfo> getBaseDao() {
		return inshQuoteinfoDao;
	}

}