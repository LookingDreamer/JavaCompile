package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCUser;

public interface INSCUserDao extends BaseDao<INSCUser> {

	public INSCUser selectByUserCode(String usercode);

	public List<Map<Object, Object>> selectUserLitByMap(Map<String, Object> map);

	public List<Map<Object, Object>> selectUserListPaging(
			Map<String, Object> map);

	public long selectPagingCount(Map<String, Object> data);

	public int updatePWDById(INSCUser user);

	/**
	 * 
	 * 批量停用用户
	 * @param user
	 * @return
	 */
	public int updateUserStatusById(INSCUser user);
	
	/**
	 * 批量启用用户
	 * @param user
	 * @return
	 */
	public int updateUserStatus2OnById(INSCUser user); 

	public INSCUser selectByUserId(String id);

	/**
	 * @param id
	 * @return
	 */
	public List<INSCUser> selectByGroupId(Map<String, Object> map);

	/**
	 * 分页得到总页数
	 * 
	 * @param map
	 * @return
	 */
	public long selectCountByParam(Map<String, Object> map);

	/**
	 * 查询不属于这些id的用户信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<INSCUser> selectRmainUsersByIds(List<String> ids);
	
	/**
	 * 根据输入的查询条件（用户编码和真实姓名），查询用户信息
	 * @param map
	 * @return
	 */
	public List<INSCUser> selectRmainUsersByMap(Map<String,Object> map);
	/**
	 * 根据输入的查询条件（用户编码和真实姓名），查询用户信息
	 * @param map
	 * @return
	 */
	public long selectCountRmainUsersByMap(Map<String,Object> map);

	/**
	 * 查询当前组织机构下所有用户带分页
	 * 
	 * @param deptId
	 * @return
	 */
	public List<INSCUser> selectUsersByDeptId(Map<String, Object> params);
	
	/**
	 * 查询数量
	 * @param params
	 * @return
	 */
	public long selectCountUsersByDeptId(Map<String, Object> params);

	/**
	 * 查询当前组织机构下所有用户数量
	 * 
	 * @param deptId
	 * @return
	 */
	public Long selectUsersCountByDeptId(Map<String, Object> params);

	/**
	 * 为群组添加成员
	 * 
	 * @return
	 */
	public int updateGroupIdById(INSCUser user);

	/**
	 * 返回id
	 * 
	 * @param user
	 * @return
	 */
	public String insertReturnId(INSCUser user);

	/**
	 * 根据群组ID获得用户信息
	 * 
	 * @param groupid
	 * @return
	 */
	public List<String> selectUserByGroupid(String groupid);
	
	
	
	/**
	 * 
	 * 组织机构相似 可以使用like查询
	 * @param deptId
	 * @return
	 */
	public List<Map<String,Object>> selectUsersUseLike(Map<String,Object> map);
	
	
	
	public long selectCountUsersUseLike(Map<String,Object> map);
	
	
	/**
	 * 
	 * 通过id得到业管code
	 * @param id
	 * @return
	 */
	public String selectCodeById(String id);
	
	/**
	 * 菜单初始化使用
	 * @param code
	 * @return
	 */
	public String selectIdByCode4Menu(String code);
	
	public List<INSCUser> queryuserlist();
	public int updateid(Map<String, String> map);
	/**
	 * 查询用户列表（业管组、所属机构）
	 * @param map
	 * @return
	 */
	public List<Map<Object, Object>> selectonlineListPaging(
			Map<String, Object> map);
	/**
	 * 根据用户的usercode查询该用户未完成任务数量
	 * @param usercode
	 * @return
	 */
	public long selectunfinishedtasknumCount(String usercode);
	
	public long selectonlineuserPagingCount(Map<String, Object> map);
	/**
	 * 根据usercode 查询所属业管组名称
	 * @param usercode
	 * @return
	 */
	public List<String> selectGroupnameByUsercode(String usercode);
	/**
	 * 根据usercode查询一个业管所属组的数量
	 * @param usercode
	 * @return
	 */
	public long selectGroupnameCountByUsercode(String usercode);
	/**
	 * 批量根据ids查询用户
	 * @param ids
	 * @return
	 */
	public List<INSCUser> selectByIds(List<String>ids);
}
