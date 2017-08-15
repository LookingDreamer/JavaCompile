package com.zzb.mobile.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.mobile.dao.INSBPayResultDao;
import com.zzb.mobile.entity.INSBPayResult;
import com.zzb.mobile.service.INSBPayResultService;



@Service
@Transactional
public class INSBPayResultServiceImpl extends BaseServiceImpl<INSBPayResult> implements
		INSBPayResultService {
	@Resource
	private INSBPayResultDao  insbPayResultDao;

	@Override
	public void insert(INSBPayResult pay) {
		pay.setId(UUIDUtils.random());
		pay.setOperator("支付平台");
		pay.setCreatetime(new Date());	
		insbPayResultDao.insert(pay);
	}

	@Override
	public INSBPayResult selectPayBybizId(String bizId) {
		// TODO Auto-generated method stub
		return insbPayResultDao.selectPayByBizId(bizId);
	}

	@Override
	public void updatePayByBizId(INSBPayResult pay) {
		// TODO Auto-generated method stub
		insbPayResultDao.updatePayByBizId(pay);
	}

	@Override
	protected BaseDao<INSBPayResult> getBaseDao() {
		return insbPayResultDao;
	}
}
