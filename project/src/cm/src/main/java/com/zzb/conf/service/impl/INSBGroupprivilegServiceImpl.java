package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBGroupprivilegDao;
import com.zzb.conf.entity.INSBGroupprivileg;
import com.zzb.conf.service.INSBGroupprivilegService;

@Service
@Transactional
public class INSBGroupprivilegServiceImpl extends BaseServiceImpl<INSBGroupprivileg> implements
		INSBGroupprivilegService {
	@Resource
	private INSBGroupprivilegDao insbGroupprivilegDao;

	@Override
	protected BaseDao<INSBGroupprivileg> getBaseDao() {
		return insbGroupprivilegDao;
	}

}