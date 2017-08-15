package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBGroupmembers;

public interface INSBGroupmembersDao extends BaseDao<INSBGroupmembers> {
	
	/**
	 * 按群组id查询
	 * @param groupid
	 * @return
	 */
	public List<INSBGroupmembers> selectByGroupId(String groupid);
	/**
	 * 按群组id查询得到用户code
	 * @param groupid
	 * @return
	 */
	public List<String> selectUserCodeByGroupId(String groupid);

	/**
	 * 按条件分页查询
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBGroupmembers> selectPageByParam(Map<String, Object> map);

	/**
	 * 得到当前查询条件查询条数
	 * 
	 * @param map
	 * @return
	 */
	public long selectPageCountByParam(Map<String, Object> map);
	
	
	/**
	 * 删除群组成员
	 * 
	 * @param userid
	 */
	public void deleteByUserId(String userid,String groupid);
	
	
	/**
	 * 通过逻辑主键得到权限信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String,String>  selectPrivilegeByGroupIdAndUserId(Map<String,String> map);
	
	/**
	 * 更新群组成员权限信息
	 * 
	 * @param map
	 * @return
	 */
	public int updataGroupPrivilegeById(Map<String, Object> map);
	
	/**
	 * 
	 * @param usercodes
	 * @return
	 */
	public List<String> selectGroupIdsByUserCodes4Task(List<String> usercodes);

	/**
	 * @param usercode
	 * @return
	 */
	public List<Map<String,String>> selectGroupIdinfosByUserCodes4Tasklist(String usercode);
	
	/**
	 * 通过业管组得到所有的业管code
	 * @param map
	 * @return
	 */
	public List<String>  selecUserCodeByGroupIds4Task(Map<String,Object> map);
	
	/**
	 * 车险任务管理页面初始化分配业管列表使用liuchao
	 * @param params
	 */
	public List<Map<String,String>>  getUserInfoListByGroup(Map<String,Object> params);
	
	/**
	 * 业管登录查询有权限的业管组
	 * @param usercodes
	 * @return
	 */
	public List<String> selectGroupIdsByUserCodes4Login(List<String> usercodes);
	
	/**
	 * 
	 * 删除业管删除业管组成员
	 * @param usercodes
	 * @return
	 */
	public List<String> selectGroupIdsByUserId4UserDelete(List<String> usercodes);
	
	
	/**
	 * 
	 * 更新群组成员usercode
	 * @return
	 */
	public int updateMembersUserCode(Map<String,String> param);
	
	
	/**
	 * 按照群组id删除群组成员信息
	 * @param groupid
	 * @return
	 */
	public int deleteByGroupId(String groupid);
	/**
	 * 查询用户业管组的权限
	 * @param param
	 */
	public long queryPrivileges(Map<String, String> param);
	
}