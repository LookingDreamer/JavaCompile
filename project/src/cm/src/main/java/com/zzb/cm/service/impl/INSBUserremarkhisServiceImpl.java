package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBUserremarkhisDao;
import com.zzb.cm.entity.INSBUserremarkhis;
import com.zzb.cm.service.INSBUserremarkhisService;

@Service
@Transactional
public class INSBUserremarkhisServiceImpl extends BaseServiceImpl<INSBUserremarkhis> implements
		INSBUserremarkhisService {
	@Resource
	private INSBUserremarkhisDao insbUserremarkhisDao;

	@Override
	protected BaseDao<INSBUserremarkhis> getBaseDao() {
		return insbUserremarkhisDao;
	}

}