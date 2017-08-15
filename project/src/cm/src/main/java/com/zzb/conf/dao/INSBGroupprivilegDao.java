package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBGroupprivileg;

public interface INSBGroupprivilegDao extends BaseDao<INSBGroupprivileg> {
	
	
	/**
	 * 通过群组id查询权限
	 * 
	 * @param groupId
	 * @return
	 */
	public List<INSBGroupprivileg> selectListByGruopId(String groupId);
	
	/**
	 * 通过群组id 删除群组管理机构关系表
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteByGroupId(String groupId);
	
	/**
	 * 通过权限code 删除群组管理机构关系表
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteByPrivilegeCodel(String privilegeCode);

}