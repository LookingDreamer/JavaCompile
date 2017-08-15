package com.zzb.extra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBProducediscountrlDao;
import com.zzb.extra.entity.INSBProducediscountrl;
import com.zzb.extra.service.INSBProducediscountrlService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBProducediscountrlServiceImpl extends BaseServiceImpl<INSBProducediscountrl> implements
		INSBProducediscountrlService {
	@Resource
	private INSBProducediscountrlDao insbProducediscountrlDao;

	@Override
	protected BaseDao<INSBProducediscountrl> getBaseDao() {
		return insbProducediscountrlDao;
	}
	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbProducediscountrlDao.getList(map);
		
		resultMap.put("total", infoList.size());
		resultMap.put("rows", infoList);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	@Override
	public void saveObject(INSBProducediscountrl INSBProducediscountrl) {
		insbProducediscountrlDao.saveObject(INSBProducediscountrl);
	}

	@Override
	public void updateObject(INSBProducediscountrl INSBProducediscountrl) {
		insbProducediscountrlDao.updateObject(INSBProducediscountrl);
	}

	@Override
	public void deleteObect(String id) {
		insbProducediscountrlDao.deleteObect(id);
	}

	@Override
	public Map findById(String id) {
		return insbProducediscountrlDao.findById(id);
	}
}