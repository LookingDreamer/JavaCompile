package com.zzb.extra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBProducediscountDao;
import com.zzb.extra.entity.INSBProducediscount;
import com.zzb.extra.service.INSBProducediscountService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBProducediscountServiceImpl extends BaseServiceImpl<INSBProducediscount> implements
		INSBProducediscountService {
	@Resource
	private INSBProducediscountDao insbProducediscountDao;

	@Override
	protected BaseDao<INSBProducediscount> getBaseDao() {
		return insbProducediscountDao;
	}
	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbProducediscountDao.getList(map);
		
		resultMap.put("total", infoList.size());
		resultMap.put("rows", infoList);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	@Override
	public void saveObject(INSBProducediscount INSBProducediscount) {
		insbProducediscountDao.saveObject(INSBProducediscount);
	}

	@Override
	public void updateObject(INSBProducediscount INSBProducediscount) {
		insbProducediscountDao.updateObject(INSBProducediscount);
	}

	@Override
	public void deleteObect(String id) {
		insbProducediscountDao.deleteObect(id);
	}

	@Override
	public Map findById(String id) {
		return insbProducediscountDao.findById(id);
	}
}