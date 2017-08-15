package com.zzb.conf.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgentpermission;

public interface INSBAgentService extends BaseService<INSBAgent> {
	/**
	 * 初始化列表页面数据 
	 *  
	 * @return
	 */
	public List<Map<String,Object>> getQueryPageData();
	/**
	 * 带分页查询条件查询
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAgentListPage(Map<String,Object> map);

	/**
	 * 初始化代理人编辑页面
	 * 
	 * @return
	 */
	public Map<String,Object> main2edit(String agentId);
	/**
	 * 绑定工号之后跟新订单保单数据
	 * @param jobno
	 * @param tempjobno
	 */
	public void updateOrderInsuranceInfo(String jobno, String tempjobno);
	/**
	 * 
	 * 保存或者新增代理人基础信息
	 * 
	 * 新增代理人 生成试用工号
	 * 
	 * @param user 操作用户
	 * @param agent 代理人信息
	 * @return
	 */
	public String saveOrUpdateAgent(INSCUser user,INSBAgent agent);



	/**
	 * 通过任务id查询车辆任务代理人信息
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getCarTaskAgentInfo(String taskId, String opType);
	/**
	 * 通过任务id查询代理人信息
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> getAgentInfo(String taskId);
	/**
	 * 初始化代理人权限关系
	 * 
	 * @param map
	 * @param agentId
	 * @return
	 */
	
	public Map<String, Object> getpermissionListByPage(Map<String, Object> map,
			String permissionsetId, String agentId);

	/**
	 * 代理人主页面转到 代理人关系表编辑页面
	 * 
	 * @return
	 */
	public INSBAgentpermission agentMian2edit(String id);

	/**
	 * 新增或者修改代理人权限关系表
	 * 
	 * @param ap
	 * @return
	 */
	public void updateAgentPermission(INSBAgentpermission ap)throws ParseException;

	/**
	 * 
	 * 删除代理人权限关系
	 * 
	 * @param ap
	 * @return
	 */
	public void removeAgentPermission(INSBAgentpermission ap);

	/**
	 * 编辑代理人供应商关系表信息
	 * 
	 * @return
	 */
	public void saveAgentProvider(String agentId,
			String providerIds);

	/**
	 * 初始化供应商
	 * 
	 * @param agentId 无功能包需要
	 * @param setId 功能包切换需要
	 * @return
	 */
	public List<Map<String, String>> getProviderTreeList(String agentId,String setId);

	/**
	 * 批量删除代理人
	 * 
	 * @param ids
	 *            字符串,分割
	 * @return
	 */
	public String deleteAgentBatch(String ids);

	/**
	 * 查询代理人信息
	 * 
	 * @param 
	 * @return
	 */
	public String getAgentInfo(Map<String, Object> map);
	
	/**
	 * 通过代理人id查询代理人工号
	 * 
	 * @param
	 * @return
	 */
	public String getAgentJobNumber(String id);
	
	/**
	 * 获取代理人认证照路径
	 * 
	 * @param jobNum 工号
	 * @return  "文件类型":"文件请求路径"
	 * 			key的取值在insccode表中获取：
	 * 		    agentqualification 代理人资格证正面照
	 * 			agentidcardpositive 代理人身份证正面照
	 * 			qualificationoppositive 代理人资格证信息页
	 * 			agentidcardopposite 代理人身份证背面照
	 * 			agentbankcard 银行卡正面照
	 * 			deptid 机构id
	 */
	public Map<String, String> getCertificationPhotoPath(String jobNum);
	/**
	 * 获取所有未认证代理人
	 * @return
	 */
	public List<Map<String,Object>> getNotCertificationAgent();
	/**
	 * 检测当前code是否重复
	 * @param agentcode
	 * @return
	 */
	public int selectcountByAgentCode(String agentcode,String id);
	/**
	 * @param certificationVo
	 * @return
	 */
	public boolean getOnlyAgent(CertificationVo certificationVo);
	public INSBAgent getAgentInfo(INSBAgent temp);
	/**
	 * 批量重置密码
	 * @param ids
	 * @return
	 */
	public Map<String, String> updateResetPwd(String ids, String operator);
	/**
	 * 处理跟踪
	 * @param paramMap
	 * @param taskstatus
	 * @return
	 */
	public String getJSONOfCarTaskListByMap(Map<String, Object> paramMap,
			String taskstatus);
	/**
	 * 查询某节点下所有网点id集合
	 * @param id
	 * @return
	 */
	public List<String> queryWDidsByPatId(String id,String comtype);
	
	/**
	 * 获得代理人
	 * @param jobnum
	 * @param deptid
	 * @return
	 */
	public INSBAgent getAgent(String jobnum);
	
	/**获取证件类型和行驶区域
	 * @return
	 */
	public Map<String, Object> getInsuranceInfo();
	
	/**
	 * 判断代理人手机号唯一性
	 * @param phone
	 * @return
	 */
	public int phoneverify(String phone, String agentid);
	
	/**
	 * 
	 */
	public void updatePwdById();
	
	/**
	 * 获取代理人所在地区
	 * @param agentnum
	 */
	public String getAgentProvince(String agentnum);
	
	/**
	 * 生成临时工号
	 * 
	 * @return
	 */
	public  String updateAgentTempJobNo();
	
	/**
	 * @param agentId 代理人id
	 * @param jobNo 代理人工号
	 * @return
	 */
	public Map<String,Object> changeAgentPwd(String token, String param);
	
	
	public Map<String, Object> setAgentAttribute(String parentcodes,String deptid);

	public Map<String, Object> getPermissionListByAgentidAndPermissionsetid(String permissionsetId, String agentId);

	public String cleanSurplusnum(String agentPermissionId,String username);

	/**
	 * 代理人密码变更发送短信(代理人信息涉及到密码变更调用此方法)
	 *
	 * @param agent
	 * @param oldPwd
	 * @return
	 */
	public int updateAgentPwd(INSBAgent agent, String oldPwd);
}
