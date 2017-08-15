package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.exception.DaoException;
import com.cninsure.system.entity.INSCRole;

public interface INSCRoleDao extends BaseDao<INSCRole> {
	public List<String> selectRolecodesByRoleids(List<String> roleidList)
			throws DaoException;

	public List<Map<Object, Object>> selectRoleLitByMap(Map<String, Object> map);

	/**
	 * 通过id得到角色名称
	 * 
	 * @param id
	 * @return
	 */
	public String selectRoleNameById(String id);
	
	/**
	 * 重写 查全部
	 * 
	 * @return
	 */
	public List<Map<String,Object>> selectAllList();
	@Deprecated
	public List<INSCRole> selectAll();

}
