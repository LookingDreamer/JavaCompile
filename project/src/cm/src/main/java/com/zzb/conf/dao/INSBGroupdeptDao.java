package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBGroupdept;
import com.zzb.conf.entity.INSBProvider;

public interface INSBGroupdeptDao extends BaseDao<INSBGroupdept> {

	/**
	 * 通过群组id查询权限
	 * 
	 * @param groupId
	 * @return
	 */
	public List<INSBGroupdept> selectListByGruopId(String groupId);
	
	/**
	 * 通过群组id 删除群组管理机构关系表
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteByGroupId(String groupId);
	
	/**
	 * 通过机构id 删除群组管理机构关系表
	 * 
	 * @param groupId
	 * @return
	 */
	public int deleteByDeptId(String deptId);

	/**
	 * 分页查询
	 * @param map
	 * @return
	 */
	public List<INSBGroupdept> selectPageByParam(Map<String, Object> map);
	
	/**
	 * 
	 * 通过网点查到业管群组
	 * 
	 * @param deptid
	 * @return
	 */
	public List<String> selectGroupIdIdByDeptId4Task(String deptid);

	/**
	 *
	 * 查询没有机构的业管组
	 *
	 * @return
	 */
	public List<String> selectNoDeptGroupIdId();

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public List<String> selectDeptIdsByGroupId(String id);
	
	/**
	 * 查询所有供应商
	 * @param groupId 
	 * @return
	 */
	public List<INSBProvider> getGroupProviderByGroup(String groupId);
	
	/**
	 * 根据DeptId查询平台协议下的父级供应商
	 * @param deptId , groupId
	 * @return
	 */
	public List<INSBProvider> getGroupProviderByDept(String deptId,String groupId);
}