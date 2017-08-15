package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgentprovider;

public interface INSBAgentproviderDao extends BaseDao<INSBAgentprovider> {
	/**
	 * 
	 * 根据代理人id 查出所有供应商id
	 * @param agentId 代理人id
	 * @return
	 */
	public List<INSBAgentprovider> selectListByAgentId(String agentId);
	
	
	/**
	 * 通过代理人id 供应商id 删除代理人供应商关系表数据
	 * @param ids
	 */
	public int deleteByAgentIdproviderId(Map<String, String> ids);
	
	/**
	 * 
	 * 根据代理人id 删除代理人供应商关系表数据
	 * @param ids
	 * @return
	 */
	public int deleteByAgentI(String agentId);

}