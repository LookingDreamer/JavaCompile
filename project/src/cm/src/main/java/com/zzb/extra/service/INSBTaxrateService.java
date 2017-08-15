package com.zzb.extra.service;

import java.util.Date;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBChnTaxrate;
/**
 * 税率service
 * @author shiguiwu
 * @date 2017年5月2日
 */
public interface INSBTaxrateService extends BaseService<INSBChnTaxrate> {
	/**
	 * 查询税率列表
	 * @param map
	 * @return
	 */
	public Map<String, Object> getTaxrateList(Map<String, Object> map);
	/**
	 * 保存或者新增税率记录
	 * @param taxrate
	 * @param operator
	 * @return
	 */
	public String saveOrUpdateTaxrate(INSBChnTaxrate taxrate, String operator, String channelidsJson);
	/**
	 * 批量删除或者单删除记录
	 * @param taxrateIds
	 * @return
	 */
	public String batchDeleteOrDeleteTaxrate(String taxrateIds);
	/**
	 * 查询税率列表，以此方法为主
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryTaxrateList(Map<String, Object> map);
	/**
	 * 根据ids，批量修改器状态
	 * @param operator
	 * @param status
	 * @param ids
	 * @return
	 */
	public String updateStatusByIds(String operator,INSBChnTaxrate taxrate);
	/**
	 * 批量复制税率到渠道
	 * @param operator
	 * @param rateIds
	 * @param channelIds
	 * @return
	 */
	public String batchCopyTaxrate(String operator, String rateIds, String channelIds, Date currtime);
	

}