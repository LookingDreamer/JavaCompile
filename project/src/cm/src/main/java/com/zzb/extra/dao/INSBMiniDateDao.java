package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMiniDate;

public interface INSBMiniDateDao extends BaseDao<INSBMiniDate> {
	
	public int insertDate(INSBMiniDate date);
	
	public List<INSBMiniDate> selectByDatetype(String datetype);

	public List<INSBMiniDate> selectDate(Map map);
	
	public List<Map<Object, Object>> queryMiniDateList(Map<String, Object> map);
	
	public long queryCountMiniDate(Map<String, Object> map);
	
	public int updateById(INSBMiniDate date);
	
	public INSBMiniDate selectDateByDatestr(Map map);
	
	public void deleteByYear(String year);

}
