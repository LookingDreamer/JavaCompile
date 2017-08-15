package com.zzb.cm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.entity.INSBCarowneinfo;

@Repository
public class INSBCarowneinfoDaoImpl extends BaseDaoImpl<INSBCarowneinfo> implements
		INSBCarowneinfoDao {

	@Override
	public INSBCarowneinfo selectByTaskId(String taskid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("select"),map);
	}

	@Override
	public List<Map<String, String>> selectMonitorInfo(String taskid, String inscomcode) {
		if(StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectMonitorInfo"),map);
	}

}