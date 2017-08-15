package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRiskrenewalitemDao;
import com.zzb.conf.entity.INSBRiskrenewalitem;
import com.zzb.conf.service.INSBRiskrenewalitemService;

@Service
@Transactional
public class INSBRiskrenewalitemServiceImpl extends BaseServiceImpl<INSBRiskrenewalitem> implements
		INSBRiskrenewalitemService {
	@Resource
	private INSBRiskrenewalitemDao insbRiskrenewalitemDao;

	@Override
	protected BaseDao<INSBRiskrenewalitem> getBaseDao() {
		return insbRiskrenewalitemDao;
	}

	@Override
	public Long queryCountVo(INSBRiskrenewalitem riskrenewalitem) {
		return insbRiskrenewalitemDao.queryCountVo(riskrenewalitem);
	}

	@Override
	public List<INSBRiskrenewalitem> queryListVo(INSBRiskrenewalitem riskrenewalitem) {
		return  insbRiskrenewalitemDao.queryListVo(riskrenewalitem);
	}

}