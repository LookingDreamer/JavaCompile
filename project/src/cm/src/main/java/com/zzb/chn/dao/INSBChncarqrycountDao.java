package com.zzb.chn.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.chn.entity.INSBChncarqrycount;

public interface INSBChncarqrycountDao extends BaseDao<INSBChncarqrycount> {
	/**
	 * 新增
	 * @param chncarqrycount
	 * @return
	 */
	public int addChncarqrycountDatas(INSBChncarqrycount chncarqrycount);
	
	/**
	 * 根据渠道id和日期day查询记录数
	 * @param map
	 * @return
	 */
	public List<INSBChncarqrycount> selectCountByCidAndDay(Map<String, Object> map); 
	
	/**
	 * 累加一天内调用次数
	 * @param map
	 * @return
	 */
	public int addCount(INSBChncarqrycount chncarqrycount); 
	public long countData(Map<String, Object> para);
}