package com.zzb.extra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBActivityprizeDao;
import com.zzb.extra.entity.INSBActivityprize;
import com.zzb.extra.service.INSBActivityprizeService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBActivityprizeServiceImpl extends BaseServiceImpl<INSBActivityprize> implements
		INSBActivityprizeService {
	@Resource
	private INSBActivityprizeDao insbActivityprizeDao;

	@Override
	protected BaseDao<INSBActivityprize> getBaseDao() {
		return insbActivityprizeDao;
	}
	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbActivityprizeDao.getList(map);
		resultMap.put("total", infoList.size());
		resultMap.put("rows", infoList);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	@Override
	public void saveObject(INSBActivityprize iNSBActivityprize) {
		insbActivityprizeDao.saveObject(iNSBActivityprize);
	}

	@Override
	public void updateObject(INSBActivityprize iNSBActivityprize) {
		insbActivityprizeDao.updateObject(iNSBActivityprize);
	}

	@Override
	public void deleteObect(String id) {
		insbActivityprizeDao.deleteObect(id);
	}

	@Override
	public Map findById(String id) {
		return insbActivityprizeDao.findById(id);
	}
	@Override
	public  List<Map<String, String>> getMap(String type){
		return insbActivityprizeDao.getMap(type);
	}

}