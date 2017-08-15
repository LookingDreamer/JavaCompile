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
import com.zzb.extra.dao.INSBUserprizeDao;
import com.zzb.extra.entity.INSBUserprize;
import com.zzb.extra.service.INSBUserprizeService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBUserprizeServiceImpl extends BaseServiceImpl<INSBUserprize> implements
		INSBUserprizeService {
	@Resource
	private INSBUserprizeDao insbUserprizeDao;

	@Override
	protected BaseDao<INSBUserprize> getBaseDao() {
		return insbUserprizeDao;
	}

	@Override
	public void saveObject(INSBUserprize insbUserprize) {
		insbUserprizeDao.saveObject(insbUserprize);
	}

	@Override
	public void updateObject(INSBUserprize insbUserprize) {
		insbUserprizeDao.updateObject(insbUserprize);
	}

	@Override
	public void deleteObect(String id) {
		insbUserprizeDao.deleteObect(id);
	}

	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbUserprizeDao.getList(map);
		
		resultMap.put("total", infoList.size());
		resultMap.put("rows", infoList);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	@Override
	public INSBUserprize findById(String id) {
		return insbUserprizeDao.findById(id);
	}

}