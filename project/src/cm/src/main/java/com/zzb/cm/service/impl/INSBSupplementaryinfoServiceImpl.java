package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBSupplementaryinfoDao;
import com.zzb.cm.entity.INSBSupplementaryinfo;
import com.zzb.cm.service.INSBSupplementaryinfoService;

@Service
@Transactional
public class INSBSupplementaryinfoServiceImpl extends BaseServiceImpl<INSBSupplementaryinfo> implements
		INSBSupplementaryinfoService {
	@Resource
	private INSBSupplementaryinfoDao insbSupplementaryinfoDao;

	@Override
	protected BaseDao<INSBSupplementaryinfo> getBaseDao() {
		return insbSupplementaryinfoDao;
	}

}