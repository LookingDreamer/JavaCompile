package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBGroupprovide;
import com.zzb.conf.entity.INSBProvider;

public interface INSBGroupprovideDao extends BaseDao<INSBGroupprovide> {
	/**
	 * 通过群组id 查询供应商
	 * @param groupId
	 * @return
	 */
	public List<INSBGroupprovide> selectListByGroupId(String groupId);
	
	/**
	 * 通过群组id 删除群组管理机构关系表
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteByGroupId(String groupId);
	
	/**
	 * 通过供应商id 删除群组管理机构关系表
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteByProviderId(String provideId);

	/**
	 *根据groupId查询关联供应商
	 * @param map
	 * @return
	 */
	public List<INSBProvider> selectprovidePageByParam(
			Map<String, Object> map);

	/**
	 * 
	 * 通过供应商得到业管群组
	 * 
	 * @param providerId
	 * @return
	 */
	public List<String> selectGroupIdByProvideid(String providerId);

	/**
	 *
	 * 查询没有供应商的业管群组
	 *
	 * @return
	 */
	public List<String> selectNoProvideGroupId();

}