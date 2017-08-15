package com.zzb.extra.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBMinichannelDao;
import com.zzb.extra.entity.INSBMinichannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBMinichannelDaoImpl extends BaseDaoImpl<INSBMinichannel> implements
		INSBMinichannelDao {

	@Override
	public long selectChannelCount(Map<String, Object> map) {
		 return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCount"), map);
	}

	@Override
	public long selectMaxTempcode() {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMaxTempcode"), new HashMap());
	}


	@Override
	public List<Map<Object, Object>> selectChannelList(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectChannelList"), map);
	}

	@Override
	public int updateWayNum(Map<String, Object> map) {
		 return this.sqlSessionTemplate.update(this.getSqlName("updateByChannelCode"), map);
	}
}