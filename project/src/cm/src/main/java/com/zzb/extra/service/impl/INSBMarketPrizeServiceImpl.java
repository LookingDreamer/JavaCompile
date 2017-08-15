package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.zzb.extra.dao.INSBMarketPrizeDao;
import com.zzb.extra.entity.INSBMarketPrize;
import com.zzb.extra.service.INSBMarketPrizeService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBMarketPrizeServiceImpl extends BaseServiceImpl<INSBMarketPrize> implements
        INSBMarketPrizeService {
	@Resource
	private INSBMarketPrizeDao insbMarketPrizeDao;

	@Override
	protected BaseDao<INSBMarketPrize> getBaseDao() {
		return insbMarketPrizeDao;
	}


	@Override
	public List<INSBMarketPrize> getPrizeList(Map<String,Object> map) {
		return insbMarketPrizeDao.getPrizeList(map);
	}

	@Override
	public void saveObject(INSBMarketPrize insbMarketPrize) {
		insbMarketPrizeDao.saveObject(insbMarketPrize);
	}

	@Override
	public void updateObject(INSBMarketPrize insbMarketPrize) {
		insbMarketPrizeDao.updateObject(insbMarketPrize);
	}

	@Override
	public void deleteObject(String id) {
		insbMarketPrizeDao.deleteObject(id);
	}

	@Override
	public Map findById(String id) {
		return insbMarketPrizeDao.findById(id);
	}

	@Override
	public String queryPrizeList(Map<String, Object> map) {
		long total = insbMarketPrizeDao.queryPrizeListCount(map);
		List<Map<Object, Object>> infoList = insbMarketPrizeDao.queryPrizeList(map);
		return ParamUtils.resultMap(total, infoList);
		//<!--add refreshrefreshrefresh-->
	}
	//<!--add refreshrefreshrefresh-->
}