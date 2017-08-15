package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBPermissionallot;

public interface INSBPermissionallotDao extends BaseDao<INSBPermissionallot> {

	public List<INSBPermissionallot> selectListBySetId(String setId);

	/**
	 * 根据权限包id返回权限类型以及对应的地址code
	 * @param setId
	 * @return
     */
	public List<Map<String, String>> selectPermissionBySetId(String setId);


	/**
	 * 新增功能包 权限关系表信息 返回新增信息主键
	 * @param allot
	 * @return
	 */
	public String insertDataReturnId(INSBPermissionallot allot);
	
	/**
	 * 删除功能包对应权限信息（删除功能包做级联删除）
	 * 
	 * @param setId
	 */
	public void deleteBySetId(String setId);
	
	/**
	 * 通过功能包id得到所有权限信息
	 * 
	 * @param setid
	 * @return 权限表id
	 */
	public List<String> selectBySetId(String setid);
}