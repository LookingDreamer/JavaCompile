package com.zzb.extra.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMiniDate;

public interface INSBMiniDateService extends BaseService<INSBMiniDate> {
	
	public int getWorkday(int day);
	
	public int insertDate(INSBMiniDate date);
	
	public List<INSBMiniDate> selectByDatetype(String datetype);

	public List<INSBMiniDate> selectDate(Map map);
	
	public int updateById(INSBMiniDate date);
	
	public INSBMiniDate selectDateByDatestr(Map map);
	
	public Map<String, Object> queryMiniDateList(Map<String, Object> map);
	
	public void deleteByYear(String year);

}
