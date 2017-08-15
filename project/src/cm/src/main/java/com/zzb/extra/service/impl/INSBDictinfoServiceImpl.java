package com.zzb.extra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBDictinfoDao;
import com.zzb.extra.entity.INSBDictinfo;
import com.zzb.extra.service.INSBDictinfoService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBDictinfoServiceImpl extends BaseServiceImpl<INSBDictinfo> implements
		INSBDictinfoService {
	@Resource
	private INSBDictinfoDao insbDictinfoDao;

	@Override
	protected BaseDao<INSBDictinfo> getBaseDao() {
		return insbDictinfoDao;
	}
	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbDictinfoDao.getList(map);
		JSONArray arr=new JSONArray();
		if(!infoList.isEmpty()){
			for(int i=0;i<infoList.size();i++){
				JSONObject jsonObject = JSONObject.fromObject(infoList.get(i));
				arr.add(jsonObject);
			}
		}
		
		return arr.toString();
	}
}