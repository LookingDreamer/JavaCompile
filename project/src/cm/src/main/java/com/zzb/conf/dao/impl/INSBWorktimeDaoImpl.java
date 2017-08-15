package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBWorktimeDao;
import com.zzb.conf.entity.INSBWorktime;

@Repository
public class INSBWorktimeDaoImpl extends BaseDaoImpl<INSBWorktime> implements
		INSBWorktimeDao {

	@Override
	public List<INSBWorktime> selectByPage(Map<Object, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByPage"),map);
	}

	@Override
	public INSBWorktime selectOneBydeptId(String inscdeptid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOneBydeptId"), inscdeptid);
	}
	
	@Override
	public List<Map<Object, Object>> selectOneFuture(Map<Object, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectWorktimeAndWeekareaInfo"), map);
	}

	@Override
	public int insertOne(INSBWorktime iscbworktime) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("insertOne"), iscbworktime);
	}

}