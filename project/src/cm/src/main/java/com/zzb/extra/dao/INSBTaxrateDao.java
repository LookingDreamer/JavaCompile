package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBChnTaxrate;
/**
 * 税率dao层
 * @author shiguiwu
 * @date 2017年5月2日
 */
public interface INSBTaxrateDao extends BaseDao<INSBChnTaxrate> {
	/**
	 * 根据条件查询税率
	 * @param map
	 * @return
	 */
	public List<INSBChnTaxrate> queryTaxrateList(Map<String, Object> map);
	/**
	 * 根据条件查询条数 
	 * @param map
	 * @return
	 */
	public Long queryTaxrate(Map<String, Object> map);
	/**
	 * 根据id更新税率的状态
	 * @param tax
	 */
	public void updateStatusById(INSBChnTaxrate tax);
	
	public List<Map<Object, Object>> queryRateLsitVo(Map<String, Object> map);
	
	public Long queryRateLsitVoCount(Map<String, Object> map);
	/**
	 * 根据条件更新税率的状态
	 * @param map
	 * @return
	 */
	public Integer updateStatusById(Map<String, Object> map);
	/**
	 * 根据渠道id查询税率
	 * @param channelid
	 * @return
	 */
	public INSBChnTaxrate queryTaxrateByChannelid(String channelid);
	/**
	 * 扫描税率状态
	 * @param channelid
	 */
	public void cleanTaxrateStatus(String channelid);

}