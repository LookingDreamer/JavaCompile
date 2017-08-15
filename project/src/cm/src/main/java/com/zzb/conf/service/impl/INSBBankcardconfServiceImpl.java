package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBBankcardconfDao;
import com.zzb.conf.entity.INSBBankcardconf;
import com.zzb.conf.service.INSBBankcardconfService;

@Service
@Transactional
public class INSBBankcardconfServiceImpl extends BaseServiceImpl<INSBBankcardconf> implements
		INSBBankcardconfService {
	@Resource
	private INSBBankcardconfDao insbBankcardconfDao;

	@Override
	protected BaseDao<INSBBankcardconf> getBaseDao() {
		return insbBankcardconfDao;
	}

	@Override
	public List<INSBBankcardconf> queryBankcardConfInfo(String paychannelid) {
		return insbBankcardconfDao.queryBankcardConfInfo(paychannelid);
	}

	@Override
	public int addBankCardConf(INSBBankcardconf bankcard) {
		return insbBankcardconfDao.addBankCardConf(bankcard);
	}

}