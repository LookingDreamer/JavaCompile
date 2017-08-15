package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRisk;

public interface INSBRiskDao extends BaseDao<INSBRisk> {
	
	public List<INSBRisk> queryByRiskVo(INSBRisk insbRisk);
	public List<INSBRisk> queryByRiskVopage(Map<String, Object> map);

	//public Long queryByRiskVoCount(INSBRisk insbRisk);
	public Long queryByRiskVoCount(Map<String, Object> map);
	
	public List<INSBRisk> selectByModifyDate(String modifydate);

	public List<Map<String,Object>> selectRiskList(String inscomcode);
	@Deprecated
	public List<INSBRisk> selectAll();
	
}