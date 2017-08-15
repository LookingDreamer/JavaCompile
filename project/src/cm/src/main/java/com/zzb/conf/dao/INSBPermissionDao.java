package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBPermission;

public interface INSBPermissionDao extends BaseDao<INSBPermission> {

	/**
	 * 功能包新增 权限
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectListByPage4add(Map<String,Object> map);
	
	/**
	 * 功能包修改权限
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectListByPage4allotupdate(Map<String,Object> map);

	/**
	 * 查询权限包 对应的权限
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectSetPermission(Map<String,Object> map);
	
	/**
	 * 代理人权限关系
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectListByPage4update(Map<String,Object> map);
	/**
	 * 根据账户类型查询权限的数量
	 * @param istry
	 * @return
	 */
	public long selectCountByIstry(Map<String,Object> map);
	/**
	 * 查询默认的权限7个（因为每类账户下都有相同权限，所以要去重）
	 * @return
	 */
	public List<INSBPermission> selectPermissionByIstry();

	public List<INSBPermission> selectAll();
	
	
}