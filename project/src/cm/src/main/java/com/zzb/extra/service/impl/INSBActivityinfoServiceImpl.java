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
import com.zzb.extra.dao.INSBActivityinfoDao;
import com.zzb.extra.entity.INSBActivityinfo;
import com.zzb.extra.service.INSBActivityinfoService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBActivityinfoServiceImpl extends BaseServiceImpl<INSBActivityinfo> implements
		INSBActivityinfoService {
	@Resource
	private INSBActivityinfoDao insbActivityinfoDao;

	@Override
	protected BaseDao<INSBActivityinfo> getBaseDao() {
		return insbActivityinfoDao;
	}

	@Override
	public String getList(Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbActivityinfoDao.getList(map);
		List<Map<Object, Object>> list=new ArrayList<Map<Object, Object>>();
		if(!infoList.isEmpty()){
			for(int i=0;i<infoList.size();i++){
				Map m=new HashMap();
				m.put("id", infoList.get(i).get("id"));
				m.put("endtime", infoList.get(i).get("endtime"));
				m.put("status", infoList.get(i).get("status"));
				m.put("conditions", infoList.get(i).get("conditions"));
				m.put("name", infoList.get(i).get("name"));
				m.put("type", infoList.get(i).get("type"));
				m.put("effectivetime", infoList.get(i).get("effectivetime"));
				m.put("createtime", "'"+infoList.get(i).get("createtime")+"'");
				m.put("limited",infoList.get(i).get("limited"));
				list.add(m);
			}
		}
		
		resultMap.put("total", infoList.size());
		resultMap.put("rows", list);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	@Override
	public void saveObject(INSBActivityinfo insbActivityinfo) {
		insbActivityinfoDao.saveObject(insbActivityinfo);
	}

	@Override
	public void updateObject(INSBActivityinfo insbActivityinfo) {
		insbActivityinfoDao.updateObject(insbActivityinfo);
	}

	@Override
	public void deleteObect(String id) {
		insbActivityinfoDao.deleteObect(id);
	}

	@Override
	public Map findById(String id) {
		return insbActivityinfoDao.findById(id);
	}

}