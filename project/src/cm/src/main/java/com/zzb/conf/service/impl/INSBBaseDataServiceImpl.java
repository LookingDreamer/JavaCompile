package com.zzb.conf.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBBaseDataDao;
import com.zzb.conf.entity.INSBBaseData;
import com.zzb.conf.service.INSBBaseDataService;

@Repository
public class INSBBaseDataServiceImpl extends BaseServiceImpl<INSBBaseData> implements INSBBaseDataService{

	@Resource
	private INSBBaseDataDao dao;
	
	@Override
	protected BaseDao<INSBBaseData> getBaseDao() {
		return null;
	}

	@Override
	public String queryBaseDataList(int page,int rows) {
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		List<INSBBaseData> result = dao.selectBaseDatas( page, rows);
		initMap.put("rows", result);
	    JSONObject jsonObject = JSONObject.fromObject(initMap);	
		return jsonObject.toString();
	}


	@Override
	public List<INSBBaseData> queryBaseDataListByModel(
			INSBBaseData model, int page, int rows) {
		return dao.selectBaseDatasByModel(model, page, rows);
	}

	@Override
	public int addBaseData(INSBBaseData model) {
		return dao.insertBaseDatas(model);
	}

	@Override
	public int removeBaseData(String id) {
		return dao.deleteBaseDatas(id);
	}

	@Override
	public int modifyBaseData(INSBBaseData baseDataModel) {
			return dao.updateBaseDatas(baseDataModel);
	}
}
