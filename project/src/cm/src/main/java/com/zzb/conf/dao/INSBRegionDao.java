package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRegion;

public interface INSBRegionDao extends BaseDao<INSBRegion> {
	public List<Map<String, String>> initList(String parentcode);
	public List<Map<String, String>> getAllCity(String parentcode);
	public List<Map<String, String>> getAllCountry(String parentcode);
	public List<Map<String, String>> getRegisterProvince();
	public List<Map<String, String>> initListfromSD(Map<String, String> para);
	public INSBRegion selectById(String id);
	public List<INSBRegion> selectByIds(List<String> ids);
}