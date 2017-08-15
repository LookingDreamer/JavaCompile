package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRiskitemDao;
import com.zzb.conf.entity.INSBRiskitem;
import com.zzb.conf.service.INSBRiskitemService;

@Service
@Transactional
public class INSBRiskitemServiceImpl extends BaseServiceImpl<INSBRiskitem> implements
		INSBRiskitemService {
	@Resource
	private INSBRiskitemDao insbRiskitemDao;

	@Override
	protected BaseDao<INSBRiskitem> getBaseDao() {
		return insbRiskitemDao;
	}

	@Override
	public Long queryCountVo(INSBRiskitem riskitem) {
		return insbRiskitemDao.queryCountVo( riskitem);
	}

	@Override
	public List<INSBRiskitem> queryListVo(INSBRiskitem riskitem) {
		return insbRiskitemDao.queryListVo( riskitem);
	}

	@Override
	public List<INSBRiskitem> selectByModifyDate(String modifydate) {
		return insbRiskitemDao.selectByModifyDate(modifydate);
	}

	@Override
	public List<INSBRiskitem> queryAll() {
		return insbRiskitemDao.selectAll();
	}

}