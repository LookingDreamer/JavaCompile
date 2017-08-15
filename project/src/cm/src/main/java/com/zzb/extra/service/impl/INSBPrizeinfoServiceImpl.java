package com.zzb.extra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBPrizeinfoDao;
import com.zzb.extra.entity.INSBPrizeinfo;
import com.zzb.extra.service.INSBPrizeinfoService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBPrizeinfoServiceImpl extends BaseServiceImpl<INSBPrizeinfo> implements
		INSBPrizeinfoService {
	@Resource
	private INSBPrizeinfoDao insbPrizeinfoDao;

	@Override
	protected BaseDao<INSBPrizeinfo> getBaseDao() {
		return insbPrizeinfoDao;
	}

	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbPrizeinfoDao.getList(map);
		resultMap.put("total", infoList.size());
		resultMap.put("rows", infoList);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	
	public Map findById(String id){
		return insbPrizeinfoDao.findById(id);
	}
	@Override
	public void saveObject(INSBPrizeinfo insbPrizeinfo) {
		insbPrizeinfoDao.saveObject(insbPrizeinfo);
	}

	@Override
	public void deleteObect(String id) {
		insbPrizeinfoDao.deleteObect(id);
	}

	@Override
	public void updateObject(INSBPrizeinfo insbPrizeinfo) {
		insbPrizeinfoDao.updateObject(insbPrizeinfo);
	}

}