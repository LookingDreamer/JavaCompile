package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.zzb.extra.dao.INSBMarketActivityPrizeDao;
import com.zzb.extra.entity.INSBMarketActivityPrize;
import com.zzb.extra.service.INSBMarketActivityPrizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBMarketActivityPrizeServiceImpl extends BaseServiceImpl<INSBMarketActivityPrize> implements
        INSBMarketActivityPrizeService {
	@Resource
	private INSBMarketActivityPrizeDao insbMarketActivityPrizeDao;

	@Override
	protected BaseDao<INSBMarketActivityPrize> getBaseDao() {
		return insbMarketActivityPrizeDao;
	}


	@Override
	public String queryActivityPrizeListById(String id) {
		return insbMarketActivityPrizeDao.queryActivityPrizeListById(id);
	}

	@Override
	public void saveObject(INSBMarketActivityPrize insbMarketActivityPrize) {
		insbMarketActivityPrizeDao.saveObject(insbMarketActivityPrize);
	}

	@Override
	public void updateObject(INSBMarketActivityPrize insbMarketActivityPrize) {
		insbMarketActivityPrizeDao.updateObject(insbMarketActivityPrize);
	}

	@Override
	public void deleteObject(String id) {
		insbMarketActivityPrizeDao.deleteObject(id);
	}

	@Override
	public long queryPagingListCount(Map<String, Object> map) {
		return insbMarketActivityPrizeDao.queryPagingListCount(map);
	}

	@Override
	public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
		return insbMarketActivityPrizeDao.queryPagingList(map);
	}//<!--add-refresh refresh->
}