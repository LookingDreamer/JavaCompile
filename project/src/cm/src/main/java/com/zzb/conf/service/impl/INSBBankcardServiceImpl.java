package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBBankcardDao;
import com.zzb.conf.entity.INSBBankcard;
import com.zzb.conf.service.INSBBankcardService;

@Service
@Transactional
public class INSBBankcardServiceImpl extends BaseServiceImpl<INSBBankcard> implements
		INSBBankcardService {
	@Resource
	private INSBBankcardDao insbBankcardDao;

	@Override
	protected BaseDao<INSBBankcard> getBaseDao() {
		return insbBankcardDao;
	}

	@Override
	public Map<String, Object> initBankCardList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbBankcardDao.selectBankCardListPaging(data);
		map.put("total", insbBankcardDao.selectCount());
		map.put("rows", infoList);
		return map;
	}

	@Override
	public int addBankCard(INSBBankcard bankcard) {
		return insbBankcardDao.addBankCard(bankcard);
	}

	@Override
	public List<Map<String, String>> selectBanknamelist() {
		return insbBankcardDao.selectBanknamelist();
	}

	@Override
	public Map<String, Object> queryByBanktoname(Map<String, Object> map) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbBankcardDao.queryByBanktoname(map);
		resultmap.put("total", infoList.size());
		resultmap.put("rows", infoList);
		return resultmap;
	}

}