package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRegion;

public interface INSBRegionService extends BaseService<INSBRegion> {
	public Map initList(String parentcode);
	public Map getAllCity(String parentcode);
	public Map getAllCountry(String parentcode);
	public Map initListfromSD(Map<String, String> para);
	public List<Map<String, String>> getRegisterProvince();
	int updateRegion(INSBRegion insbRegion, String operator);
	List<Map<String, Object>> initRegion(String parentcode);
}