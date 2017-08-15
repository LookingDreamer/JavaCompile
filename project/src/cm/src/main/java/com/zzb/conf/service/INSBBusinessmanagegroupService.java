package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBBusinessmanagegroup;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBTaskprivilege;

public interface INSBBusinessmanagegroupService extends
		BaseService<INSBBusinessmanagegroup> {

	public Map<String, Object> queryByParamPage(Map<String, Object> map);

	/**
	 * 通过群组id查出所有权限信息（一棵树）
	 * 
	 * @param gId
	 * @return
	 */
	public List<INSBTaskprivilege> queryTreeByPrivilegePcode(String pcode);

	/**
	 * 初始化新增页面数据
	 * 
	 * @return
	 */
	public Map<String, Object> main2add();

	/**
	 * @param groupName
	 * @param pid
	 * @param groupKind
	 * @return
	 */
	public Map<String, Object> common2detail(String groupName, String pid,
			String groupKind);

	/**
	 * 
	 * 初始化机构树
	 * 
	 * @param pcode
	 *            父群组机构id
	 * @return
	 */
	public List<Map<String, String>> queryTreeByPcode(String parentcode);

	/**
	 * 
	 * 新增群组
	 * 
	 * @param gropData
	 *            群组基本信息
	 * @param pcode
	 *            权限编码
	 * @param deptids
	 *            管理机构id
	 * @param providerIds
	 *            管理供应商id
	 * @param pramMap
	 *            群组特殊字段
	 */
	public void saveGroupData(INSCUser user,INSBBusinessmanagegroup gropData, String[] pcode,
			String deptids, String providerIds, Map<String, Object> pramMap);
	
	
	/**
	 * 
	 * 保存群组基本数据
	 * @param user
	 * @param gropData
	 */
	public void saveBaseGroupData(INSCUser user,INSBBusinessmanagegroup gropData);

	/**
	 * 准备编辑页面数据
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> updateGruopData(String id);

	/**
	 * 得到供应商树
	 * 
	 * @param parentcode
	 * @param agentId
	 * @param checked
	 * @param isSet
	 * @return
	 */
	public List<Map<String, String>> getProviderTreeList(String parentcode,
			String providerIds, String checked);

	/**
	 * 
	 * 批量删除群组信息
	 * 
	 * @param ids
	 * @return
	 */
	public void deleteGroupBath(String ids);

	/**
	 * 初始化群组成员添加 页面
	 * 
	 * @param groupId
	 * @return
	 */
	public Map<String, Object> getGroupMenberData(String groupId);

	/**
	 * 初始化当前群组成员
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getGroupMemberList(Map<String, Object> map);

	/**
	 * 根据当前群组得到所有符合条件的群组成员列表
	 * 
	 * @param groupId 当前群组id
	 * @param map 分页信息
	 * @param usercode 业管账号
	 * @param name 业管姓名
	 * @param deptType 组织机构选择范围
	 * @param grade 层级 组织机构详细过滤
	 * @return
	 */
	public Map<String, Object> queryUsetListByGroupId(String groupId,Map<String, Object> map,String usercode,String name,String deptid);

	/**
	 * 为群组添加成员
	 * 
	 * @param userIds
	 * @param groupId
	 */
	public void saveGroupUsers(String userIds, String groupId);
	

	/**
	 * 批量移除群组成员
	 * 
	 * @param groupId
	 * @param userIds
	 */
	public void removeGroupMember(String userIds, String groupId);

	/**
	 * 通过 群组id userid得到 user信息权限信息
	 * 
	 * @param groupId
	 * @return
	 */
	public Map<String,Object> queryGroupPrivilegeByGroupId(String userId,String groupId);

	/**
	 * 更新群组成员权限信息
	 * 
	 * @param id
	 * @param groupprivilege
	 */
	public void updatGroupMemberPrivilege(String id, String groupprivilege);

	/**
	 * 通过任务组ID获得业管群组信息
	 * 
	 * @param tasksetid
	 * @return
	 */
	public List<INSBBusinessmanagegroup> findByTasksetid(
			String tasksetid);

	/**
	 * 初始化当前群组的网点
	 * @param map
	 * @return
	 */
	public Map<String, Object> getGroupDeptidList(Map<String, Object> map,String groupId);

	/**初始化群组供应商
	 * @param map
	 * @return
	 */
	public Map<String, Object> getGroupProviderList(Map<String, Object> map,String groupId);
	/**
	 * 批量移除群组供应商
	 * 
	 * @param groupid
	 * @param Ids
	 */
	public void removeGroupProvide(String ids, String groupid);
	/**
	 * 批量移除群组供应商
	 * 
	 * @param groupid
	 * @param Ids
	 */
	public void removeGroupDept(String ids, String groupid);
	
	/**
	 * 通过业管code查询同组所有业管
	 * @param member
	 * @return
	 */
	public List<Map<String,String>> getMembersByMember(INSCUser member);
	
	/**
	 * 通过平台id查询父级供应商
	 * @param organizationid
	 * @return
	 */
	public List<Map<String, Object>> queryGroupProviderByDeptId(String deptId, String groupId,String checked);

}