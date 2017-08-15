package com.zzb.extra.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBMinichannelwayDao;
import com.zzb.extra.entity.INSBMinichannelway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBMinichannelwayDaoImpl extends BaseDaoImpl<INSBMinichannelway> implements
		INSBMinichannelwayDao {
	public List<Map<Object,Object>> selectChannelWayList(Map<String,Object> map){
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectpage"), map);
	}
	@Override
	public long selectMaxWayCode(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMaxWayCode"), map);
	}
}