package com.zzb.conf.dao;
  
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBPermissionset;

public interface INSBPermissionsetDao extends BaseDao<INSBPermissionset> {
	public List<INSBPermissionset> selectListPage(Map<String,Object> map);
	
	/**
	 * 新增 返回id
	 * @param set
	 * @return
	 */
	public String insertReturnId(INSBPermissionset set);

	
	/**
	 * 按条件查询得到总条数
	 * 
	 * @param map
	 * @return
	 */
	public long selectCountByParam(Map<String, Object> map);
	
	/**
	 * 查询已经启用的功能包
	 * 
	 * @return
	 */
	public List<INSBPermissionset> selectByOnUseSet();
	
	
	/**
	 * 通过网点  账户类型过滤 得到 已经启用的权限
	 * 
	 * @param deptid  网点
	 * @param istest 账户类型
	 * @return
	 */
	public List<String> selectByDeptId(String deptid,Integer istest);
	
	/**
	 * 查询默认的权限包
	 * @param istest 账户类型
	 * @return
	 */
	public List<String> selectDefaultPermissionallot(int istest);
	
	/**
	 * 根据代理人所属机构，用户类型筛选出启用的功能包
	 * @param deptid
	 * @param agentkind
	 * @return
	 */
	public List<INSBPermissionset> selectByDeptAgentkindAndOnUseSet(String deptid,int agentkind);

	/**
	 * 根据代理人所属机构，筛选出启用的试用功能包
	 * @param deptid
	 * @return
	 */
	public List<INSBPermissionset> selectTrialSet(String deptid);

	/**
	 * 根据权限包id，筛选出关联的用户
	 * @param setId
	 * @return
	 */
	public List<Map<String,Object>> getUserByPage(Map<String,Object> map);

	/**
	 * 根据权限包id，筛选出关联的用户数量
	 * @param setId
	 * @return
	 */
	public long getUserByPageCount(Map<String,Object> map);

	/**
	 *  判断权限包代码唯一性
	 * @param map
	 * @return
	 */
	public int  selectCountBySetCode(Map<String, Object> map);

	/**
	 * 查询部门下启用的权限包
	 * @param map
	 * @return
     */
	public List<INSBPermissionset> selectByDeptinnercode(Map<String, Object> map);
	
	/**
	 * 根据代理人所属机构和用户类型，筛选出启用的权限包
	 * @param deptid
	 * @param agentkind
	 * @return
	 */
	public List<INSBPermissionset> selectSetByKindAndDeptid(String deptid, String agentkind);


	public String queryBytrysetcode(String tryset);

	public String queryByformalsetcode(String formalset);

	public String queryBychannelsetcode(String channelset);

	public List<INSBPermissionset> selectByDeptAndAgentkind(Map<String, Object> param);

}