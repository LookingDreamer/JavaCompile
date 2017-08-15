package com.zzb.chn.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.chn.entity.INSBChncarqry;

public interface INSBChncarqryDao extends BaseDao<INSBChncarqry> {
	/**
	 * 新增
	 * @param chncarqry
	 * @return
	 */
	public int addChncarqryDatas(INSBChncarqry chncarqry);
	
	/**
	 * 创建分表
	 * @param chncarqry
	 * @return
	 */
	public int createSubTable(INSBChncarqry chncarqry); 
	public List<Map<String, Object>> queryDetail(Map<String, Object> para);
	public long queryDetailSize(Map<String, Object> para);
	public long queryClnGroupCount(Map<String, Object> para);
	public List<Map<String, Object>> queryGroupRecCount(Map<String, Object> para);
	public long queryGroupRecCountSize(Map<String, Object> para);
	public List<Map<String, Object>> querySummaryCount(Map<String, Object> para);
	public long queryCount(Map<String, Object> para);
	public Map<String, Object> queryMaxTimeRec(Map<String, Object> para);
}