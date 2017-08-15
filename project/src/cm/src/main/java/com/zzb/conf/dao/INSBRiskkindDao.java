package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRiskkind;

public interface INSBRiskkindDao extends BaseDao<INSBRiskkind> {

	public Long queryListByVoCount(INSBRiskkind riskkind);
	public Long queryListByVoCount(Map<String, Object> map);

	public List<INSBRiskkind> queryListByVo(INSBRiskkind riskkind);
	public List<INSBRiskkind> queryByRiskkindVopage(Map<String, Object> map);
	
	public List<INSBRiskkind> selectByModifyDate(String modifydate);
	/**
	 * liuchao查询排序后的险种
	 * */
	public List<Map<String, String>> selectOrderedRiskkindByInscomcode(String inscomcode);
	
	/**
	 * 通过险别编码查询当前险种是否存在重复险别
	 * @param kindcode
	 * @return
	 */
	public long selectCOuntByKindCode(Map<String,String> map);
	
	public String selectKindNameByCode(String code);
	
	public List<String> selectAllPreKind();
	/**
	 * 查某险种下的险别数量
	 * @param riskid
	 * @return
	 */
	public int selectCountByRiskid(String riskid);	
	/**
	 * 判断当前riskid下 是否存在相同的code 
	 * @param map
	 * @return
	 */
	public int selectCountByKindcode(Map<String,String> map);
	/*
	 * 根据前置险种查询不计免赔险种信息
	 */
	public INSBRiskkind notDedByPreriskkind(Map<String,String> map);
	@Deprecated
	public List<INSBRiskkind> selectAll();
} 