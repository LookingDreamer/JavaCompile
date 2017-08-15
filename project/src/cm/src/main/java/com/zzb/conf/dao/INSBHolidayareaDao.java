package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBHolidayarea;

public interface INSBHolidayareaDao extends BaseDao<INSBHolidayarea> {

	public List<INSBHolidayarea> selectByWorktimeId(Map<String, String> map3);

	List<Map<Object, Object>> selectFuture(Map<Object, Object> htmap);

}