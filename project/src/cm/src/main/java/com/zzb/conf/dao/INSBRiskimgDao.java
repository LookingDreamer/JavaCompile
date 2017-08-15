package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.entity.INSBRiskimg;

public interface INSBRiskimgDao extends BaseDao<INSBRiskimg> {

	public Long queryCountVo(INSBRiskimg riskimg);

	public List<INSBRiskimg> queryListVo(INSBRiskimg riskimg);
	
	public void deleteByRiskId(String riskid);
	
	public List<INSCCode> getDefaultRiskImg();
	
	public List<INSBRiskimg> queryListByVopage(Map<String, Object> initMap);
	
	/**
	 * 通过险种id查询该险种下的影像类型
	 * @param riskid
	 * @return
	 */
	public List<INSBRiskimg> selectRiskimgByRiskid(String riskid);
	
	/**
	 * 通过险种id查询该险种下的影像类型的type
	 * @param riskid
	 * @return
	 */
	public List<String> selectRiskimgtypeByRiskid(String riskid);
	
	/**
	 * 
	 * @param urId
	 * @return
	 */
	public int deleteByRiskimgtypeRiskid(Map<String,String> urId);
	/**
	 * 通过险种id查询还未新增的影像类型
	 * @param riskid
	 * @return
	 */
	public List<INSCCode> selectNotSelectedRiskimgtypeByRiskid(String riskid);
}