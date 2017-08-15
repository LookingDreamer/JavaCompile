package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBChannelcarinfoDao;
import com.zzb.conf.entity.INSBChannelcarinfo;

@Repository
public class INSBChannelcarinfoDaoImpl extends BaseDaoImpl<INSBChannelcarinfo> implements
		INSBChannelcarinfoDao {

	@Override
	public List<INSBChannelcarinfo> getChannelcarinfo(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getChannelcarinfoByMap"),map);
	}

}