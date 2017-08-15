package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBWorktimeareaDao;
import com.zzb.conf.entity.INSBWorktimearea;

@Repository
public class INSBWorktimeareaDaoImpl extends BaseDaoImpl<INSBWorktimearea> implements
		INSBWorktimeareaDao {

	@Override
	public List<INSBWorktimearea> selectByWorktimeId(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByWorktimeId"),map);
	}

}