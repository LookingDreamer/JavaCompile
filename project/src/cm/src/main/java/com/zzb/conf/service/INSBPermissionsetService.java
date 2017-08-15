package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.controller.vo.ExportPermissionInfoVo;
import com.zzb.conf.entity.INSBPermissionset;

public interface INSBPermissionsetService extends BaseService<INSBPermissionset> {
	/**
	 * 带分页查询
	 * 
	 * TODO 连机构表拿名称
	 * @param map
	 * @return
	 */
	public  Map<String,Object> getPermissionsetListByPage(Map<String,Object> map);
	
	
	/**
	 * 1:初始化 得到当前父节点选中状态 设置ZTree选中
	 * 
	 * 2:使当前子节点为父节点 得到子节点 
	 * 2.1:判断当前拿到子节点是否在 关系表中全部存在
	 * 2.1.1:全部存在 父节点选中，子节点全部选中
	 * 2.1.2：有部分子节点
	 * 
	 * @param parentcode
	 * @param permissionSetId
	 * @param checked
	 * @return
	 */
	public List<Map<String,String>> getProviderTreeList(String parentcode,String permissionSetId,String checked);
	
	
	/**
	 * 得到权限列表信息
	 * 
	 * 
	 * 
	 * 根据当前功能包得到 关系表信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getpermissionListByPage(Map<String,Object> map,String permissionsetId);

	/**
	 * 权限包关联用户
	 * @param map
	 * @param permissionsetId
     * @return
     */
	public Map<String, Object> getUserListByPage(Map<String,Object> map,String permissionsetId);

	/**
	 * 
	 * TODO
	 * 新增功能包  功能分配（关系表） 
	 * @param map
	 * @return
	 */
	public Map<String,String> savePermissionsetAllot(Map<String,Object> map);
	
	
	/**
	 * 功能包 转到编辑页面 初始化数据信息
	 * @param id 功能包id
	 */
	public Map<String,Object> main2edit(String id);
	
	
	/**
	 * 
	 * 新增或者修改 功能包基本信息
	 * 功能包状态变为 未启用  需重新启用
	 * 
	 * @param set
	 * @return
	 */
	public String saveOrUpdateSetReturnId(INSBPermissionset set);
	
	
	/**
	 * 
	 * @param allotIds
	 * @param providerIds
	 * @return
	 */
	public void saveSetProviderAllotData(String setId,String providerIds,String opFlag);
	
	/**
	 * 同步代理人关系表
	 * 
	 * 1：新增功能包
	 * 1.1：按照逻辑主键查找所有符合条件的代理人 ，为代理人添加功能包id
	 * 1.2：代理人对应的权限关系表 权限等于 功能包中设置的权限
	 * 1.3：代理人修改权限 没有的新增  有的不动 多的的删除
	 * 
	 * 
	 * 2：新增代理人 选择功能包（权限  供应商都不能修改）
	 * 2.1：自定义权限（全部是新增没有关系）
	 * 
	 * 3：修改权限包 权限修改（只是维护代理人中有权限包这类人）
	 * 3.1：需要同步关系表
	 * 
	 * @return
	 */
	public void updateAgentTable(String setId,String isNew);
	
	
	/**
	 * 如果当前功能包启用过 不允许删除 只能停用
	 * 
	 * @param setId
	 * @return
	 */
	public int deleteJudge(String setId);
	
	/**
	 * 查询所有已经启用的功能包
	 * 
	 * @return
	 */
	public List<INSBPermissionset> queryOnUseSet();
	
	/**
	 * 新注册代理人 初始化权限 （已经停用）
	 * 
	 * @param deptId  代理人所属网点
	 * @param jobNo 代理人工号（临时工号）
	 * @return 1:根据代理人网点权限初始化成功 2：未找到对应初始化功能包（初始化通用代理人权限）
	 */
	@Deprecated
	public int initTestAgentPermission(String deptId,String jobNo);
	
	/**
	 * 正式代理人初始化权限（已经停用）
	 * 
	 * @param deptId  代理人所属网点
	 * @param jobNo 代理人工号（临时工号）
	 * @return 1:根据代理人网点权限初始化成功 2：未找到对应初始化功能包（初始化通用代理人权限）
	 */
	@Deprecated
	public int initAgentPermission(String deptId,String jobNo);
	
	
	
	
	/**
	 * 调用条件：任何功能包修改
	 * 
	 * 1：更新功能包状态为停用
	 * 2：解除功能包绑定
	 * 
	 * @param set 解除代理人中相同功能包id
	 */
	public void releaseBandingByLogicId(INSBPermissionset set);
	
	
	/**
	 * 根据代理人所属机构，账户类型筛选出启用的功能包
	 * @param deptid
	 * @param agentkind
	 * @return
	 */
	public List<INSBPermissionset> selectByDeptAgentkindAndOnUseSet(String deptid,Integer agentkind);
	
	/**
	 *  判断权限包代码唯一性
	 * @param
	 * @return
	 */
	public int  selectCountBySetCode(String setcode,String id);

	/**
	 * 当某机构或网点应用了某个功能包后，代表该机构或网点下的权限功能包指定用户类型（如：试用）的用户将赋予此权限包的功能权限。
	 * 例如：给广东南枫机构应用了一个权限包，该权限包对应的用户类型是试用，则广东南枫下所有用户类型为试用的用户，都应用了该权限包。
	 * @param setid
	 * @param deptid
	 * @param operator
     * @return
     */
	public boolean saveFuncs(String setid, String deptid, String operator);

	/**
	 * 列出该部门下的权限包
	 * @param deptid
	 * @return
     */
	public List<INSBPermissionset> selectPermissionset(String deptid);

	/**
	 * 批量关联
	 */
	public List<ExportPermissionInfoVo> importAgentRelationPermission(String permissionsetid, List<String> agentInfoList,String user) throws Exception;
	
	/**
	 * 导出批量关联结果
	 */
	public ResponseEntity<byte[]> exPortResult(String str);

	/**
	 * 通过机构查询权限包
	 * @param parentcodes
	 * @return
	 */
	public List<INSBPermissionset> querybyparentcodes(String deptid, String agentkind);

}