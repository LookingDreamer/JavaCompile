package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;

public interface INSCUserService extends BaseService<INSCUser> {

	public INSCUser queryByUserCode(String usercode);

	public boolean userCodeCheck(String usercode);

	public void benchDeleteByIds(List<String> arrayid);

	public boolean changePassword(String usercode, String oldpwd, String newpwd);
	/**
	 * 初始化用户列表
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> initUserList(Map<String, Object> map);

	/**
	 * 重置密码（可批量操作）
	 * 
	 * @return
	 */
	public Map<String, String> updateResetPwd(String userIds);

	/**
	 * 用户状态停用（可批量操作）
	 * 
	 * @return
	 */
	public Map<String, String> updateResetUserSataus(String userIds,int type);

	/**
	 * 根据群组ID获得用户信息
	 * 
	 * @param groupid
	 * @return
	 */
	public List<String> findUserByGroupid(String groupid) ;
	
	
	/**
	 * 初始化编辑页面
	 * 
	 * @return
	 */
	public Map<String,Object> getEditeData(String id);
	
	/**
	 * 新增或者修改用户信息
	 * 
	 * @param operator 操作人
	 * @param user
	 * @param roleIds
	 */
	public void saveOrUpdate(INSCUser operator,INSCUser user,String roleIds);
	
	public INSCUser getByUsercode(String code);
	
	/**
	 * 在线用户列表
	 */
	public Map<String, Object> initonlineList(Map<String, Object> map);
	
	/**
	 * 级联删除
	 * @param id
	 * @return
	 */
	public int cascadDelete(String id);
	/**
	 * 修改用户最大处理任务数
	 * @param usercode
	 * @param taskpool
	 * @return
	 */
	public boolean changePooltasks(String usercode, String taskpool);
}
