package com.zzb.extra.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBAgentBankcardDao;
import com.zzb.extra.entity.INSBAgentBankcard;
import com.zzb.extra.entity.INSBAgentDeliveryaddress;
import com.zzb.extra.service.INSBAgentBankcardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBAgentBankcardServiceImpl extends BaseServiceImpl<INSBAgentBankcard> implements
		INSBAgentBankcardService {
	@Resource
	private INSBAgentBankcardDao insbAgentBankcardDao;

	@Override
	protected BaseDao<INSBAgentBankcard> getBaseDao() {
		return insbAgentBankcardDao;
	}

	@Override
	public Map<String, Object> initAgentBankCardList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbAgentBankcardDao.selectAgentBankCardListPaging(data);
		List<INSBAgentBankcard> rows = new ArrayList<>();
		
		Iterator<Map<Object, Object>> iterator = infoList.iterator();
		while (iterator.hasNext()) {
			INSBAgentBankcard insbAgentBankcardTmp = (INSBAgentBankcard)iterator.next();
			
			String noti = insbAgentBankcardDao.selectNameByCardagentId(data);
			
			insbAgentBankcardTmp.setNoti(noti);
			
			rows.add(insbAgentBankcardTmp);
			
			
		}
		map.put("total", insbAgentBankcardDao.selectCount());
		map.put("rows", rows);
		return map;
	}

	@Override
	public List<Map<Object, Object>> agentBankcardquerybyagentid(Map<String, Object> data) {
		//Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbAgentBankcardDao.selectAgentBankCardByCardagentid(data);
		//map.put("rows", infoList);
		return infoList;
	}

	@Override
	public int addAgentBankCard(INSBAgentBankcard agentBankcard) {
		return insbAgentBankcardDao.addAgentBankCard(agentBankcard);
	}

	@Override
	public void setDefaultCard(String id, String cardagentid){
		insbAgentBankcardDao.setDefaultCardByCardagentid(cardagentid,"0");
		insbAgentBankcardDao.setDefaultCardByCardId(id,"1");
	}

	@Override
	public List<Map<String, String>> selectAgentBanknamelist() {
		return insbAgentBankcardDao.selectAgentBanknamelist();
	}

	@Override
	public Map<String, Object> queryByAgentBanktoname(Map<String, Object> map) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbAgentBankcardDao.queryByAgentBanktoname(map);
		resultmap.put("total", infoList.size());
		resultmap.put("rows", infoList);
		return resultmap;
	}

}