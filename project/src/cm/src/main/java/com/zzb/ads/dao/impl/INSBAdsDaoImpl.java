package com.zzb.ads.dao.impl;

import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.ads.dao.INSBAdsDao;
import com.zzb.ads.entity.INSBAds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBAdsDaoImpl extends BaseDaoImpl<INSBAds> implements
		INSBAdsDao {

	@Override
	public List<Map<String, String>> getAds(String taskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getAds"), taskid);
	}

	@Override
	public int deleteAds(String taskid, String agreementid) {
		Map map = new HashMap();
		map.put("taskid", taskid);
		map.put("agreementid", agreementid);
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteAds"), map);
	}
}