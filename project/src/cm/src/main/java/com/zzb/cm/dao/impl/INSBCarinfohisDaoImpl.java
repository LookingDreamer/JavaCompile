package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.entity.INSBCarinfohis;

import java.util.HashMap;
import java.util.Map;

@Repository
public class INSBCarinfohisDaoImpl extends BaseDaoImpl<INSBCarinfohis> implements INSBCarinfohisDao {

	public boolean updateInsureconfigsameaslastyear(String taskid, String inscomcode, String value) {
		Map<String, String> params = new HashMap<>(3);
		params.put("taskid", taskid);
		params.put("inscomcode", inscomcode);
		params.put("sameaslastyear", value);
		int result = this.sqlSessionTemplate.update(this.getSqlName("updateInsureconfigsameaslastyear"), params);
		return result>0;
	}

	@Override
	public INSBCarinfohis selectCarinfohis(INSBCarinfohis insbCarinfohis){ 
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCarinfohis"),insbCarinfohis);
	}
}