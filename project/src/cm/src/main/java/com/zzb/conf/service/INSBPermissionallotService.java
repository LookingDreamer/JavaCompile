package com.zzb.conf.service;

import java.util.List;
import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.controller.vo.AgentRelatePermissionVo;
import com.zzb.conf.entity.INSBPermissionallot;
import com.zzb.conf.entity.INSBPermissionset;

/**
 * 权限分配
 * @author Administrator
 *
 */
public interface INSBPermissionallotService extends BaseService<INSBPermissionallot> {
	
	/**
	 * 新增修改 权限分配表
	 * 
	 * 新增返回id
	 * @param allot
	 * @return
	 */
	public String saveOrUpdate(INSBPermissionallot allot,INSCUser user);

	/**
	 * 通过代理人id，获取代理相关的权限包下的权限
	 * @param agentId
	 * @return
	 */
	public List<AgentRelatePermissionVo> getPermissionallotByAgentId(String agentId);

	public INSBPermissionset getPermissionsetByAgentId(String agentId);

}