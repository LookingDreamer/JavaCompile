package com.zzb.cm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.entity.INSBCarmodelinfohis;

@Repository
public class INSBCarmodelinfohisDaoImpl extends BaseDaoImpl<INSBCarmodelinfohis> implements
		INSBCarmodelinfohisDao {

	@Override
	public List<INSBCarmodelinfohis> selectPageByStandardname(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByStandardname"), map);
	}
	
	@Override
	public long selectCountPageByStandardname(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByStandardname"), map);
	}

	@Override
	public void insertCarmodelhis(Map<String, Object> carinfohis) {
		this.sqlSessionTemplate.selectOne(this.getSqlName("insertCarmodelhis"), carinfohis);
	}

	@Override
	public INSBCarmodelinfohis selectModelInfoByTaskIdAndPrvId(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectModelInfoByTaskIdAndPrvId"), map);
	}

	public INSBCarmodelinfohis selectModelInfoByTaskIdAndPrvId(String taskid, String prvid) {
		Map<String, Object> param = new HashMap<>();
		param.put("taskid", taskid);
		param.put("inscomcode", prvid);
		return this.selectModelInfoByTaskIdAndPrvId(param);
	}
}