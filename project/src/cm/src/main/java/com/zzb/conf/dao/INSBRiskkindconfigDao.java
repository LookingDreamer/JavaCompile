package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRiskkindconfig;

public interface INSBRiskkindconfigDao extends BaseDao<INSBRiskkindconfig> {

	
	/**
	 * 查询出一条
	 * 
	 * @param datamap
	 * @return
	 */
	public INSBRiskkindconfig selectByDatamp(String datamap);
	/**
	 * 设置默认险别为选中的这28条，而不要默认全部
	 * @return
	 */
	public List<INSBRiskkindconfig> selectNotAll();
	/**
	 * 险别新增里的通过险别编码查询险别。
	 * @param riskkindcode
	 * @return
	 */
	public INSBRiskkindconfig selectKindByKindcode(String riskkindcode);
	@Deprecated
	public List<INSBRiskkindconfig> selectAll();
	
	public List<INSBRiskkindconfig> selectListIn(Map<String, Object> params);
}