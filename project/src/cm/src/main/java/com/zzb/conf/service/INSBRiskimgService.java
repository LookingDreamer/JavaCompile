package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.entity.INSBRiskimg;

public interface INSBRiskimgService extends BaseService<INSBRiskimg> {
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
	 * 新增或者修改影像类型信息
	 * 
	 * @param riskimg
	 * @param riskimgtype
	 */
	public void saveOrUpdate(INSBRiskimg riskimg,String riskimgtype);
	
	/**
	 * 通过险种id查询还未新增的影像类型
	 * @param riskid
	 * @return
	 */
	public List<INSCCode> selectNotSelectedRiskimgtypeByRiskid(String riskid);
}