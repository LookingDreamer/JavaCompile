package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBHolidayareaDao;
import com.zzb.conf.entity.INSBHolidayarea;

@Repository
public class INSBHolidayareaDaoImpl extends BaseDaoImpl<INSBHolidayarea> implements
		INSBHolidayareaDao {

	@Override
	public List<INSBHolidayarea> selectByWorktimeId(Map<String, String> map3) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByWorktimeId"),map3);
	}
	
	@Override
	public List<Map<Object, Object>> selectFuture(Map<Object, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectFuture"),map);
	}

}