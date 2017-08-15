package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskkind;

public interface INSBRiskService extends BaseService<INSBRisk> {
	
	public List<INSBRisk> queryListByVo(INSBRisk insbRisk);
	public Map<String, Object> queryListByVopage(Map<String, Object> map);
	
	//public Object queryListByVoCount(INSBRisk risk);
	public Object queryListByVoCount(Map<String, Object> map);
	
	public List<INSBRisk> selectByModifyDate(String modifydate);
	
	
	/**
	 * 根据险别定义更新riskkindconfig表
	 * 
	 * @param kindtype
	 */
	public void updateRiskKindConfig(INSCUser operator,INSBRiskkind riskkind);
	
	
	/**
	 * 初始化当前险种通用险别
	 * @param riskid
	 */
	public int initData(String riskid);
	
	/**
	 * 批量删除险种信息
	 */
	public void deleteRiskByIds(List<String> arrayid);
	
	/**
	 * 得到当前险别 前置险别
	 * @param riskkindId
	 * @return
	 */
	public List<String> getPreRiskKindName(String riskkindId);
	/**
	 * 根据前置险别编码得到前置险别名称
	 * @param code
	 * @return
	 */
	public String selectKindNameByCode(String code);
	/**
	 * 分页展示险别列表
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryKindListByVopage(Map<String, Object> map);
	@Deprecated
	public List<INSBRisk> queryAll();
	
}