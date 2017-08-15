package com.zzb.extra.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.model.CommonModel;

public interface INSBAgentUserService extends BaseService<INSBAgent> {
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
	public Map<String, Object> getAgentUserListPage(Map<String, Object> map);
	public Map<String, Object> getAgentPhoneUserListPage(Map<String, Object> map);

	/**
	 * 初始化代理人编辑页面
	 * 
	 * @return
	 */
	public Map<String,Object> main2edit(String agentUserId);


	/**
	 * 证件照上传
	 *
	 * @param file
	 *            上传的文件(Base64格式)
	 * @param fileName
	 *            上传的文件名
	 * @param fileType
	 *            文件类型
	 * @param fileDescribes
	 *            文件描述
	 * @param jobNum
	 *            代理人用户id
	 * @return
	 *  参考：com.zzb.mobile.service.AppRegisteredService.fileUpLoadBase64
	 */
	public CommonModel fileUpLoadBase64(HttpServletRequest request, String file, String fileName, String fileType,
										String fileDescribes, String jobNum, String taskId);

	/*
	*  参考：com.zzb.mobile.service.AppRegisteredService.fileUpLoadWeChat
	*/
	public CommonModel fileUpLoadWeChat(HttpServletRequest request, String mediaId, String fileName, String fileType,
										String fileDescribes, String jobNum, String taskId) ;

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
//	public String saveOrUpdateAgentUser(INSCUser user, INSBAgent agent);

//	/**
//	 * 通过任务id查询车辆任务代理人信息
//	 *
//	 * @param taskId
//	 * @return
//	 */
//	public Map<String, Object> getCarTaskAgentUserInfo(String taskId, String opType);
//	/**
//	 * 通过任务id查询代理人信息
//	 * @param taskId
//	 * @return
//	 */
//	public Map<String,Object> getAgentUserInfo(String taskId);
	/**
	 * 初始化代理人权限关系
	 * 
	 * @param map
	 * @param agentUserId
	 * @return
	 */
//	public Map<String, Object> getpermissionListByPage(Map<String, Object> map,
//													   String permissionsetId, String agentUserId);

	/**
	 * 代理人主页面转到 代理人关系表编辑页面
	 * 
	 * @return
	 */
//	public INSBAgentpermission agentMian2edit(String id);

	/**
	 * 新增或者修改代理人权限关系表
	 * 
	 * @param ap
	 * @return
	 */
//	public void updateAgentPermission(INSBAgentpermission ap)throws ParseException;

	/**
	 * 
	 * 删除代理人权限关系
	 * 
	 * @param ap
	 * @return
	 */
//	public void removeAgentPermission(INSBAgentpermission ap);

	/**
	 * 编辑代理人供应商关系表信息
	 * 
	 * @return
	 */
//	public void saveAgentProvider(String agentId,
//								  String providerIds);

	/**
	 * 初始化供应商
	 * 
	 * @param agentId 无功能包需要
	 * @param setId 功能包切换需要
	 * @return
	 */
//	public List<Map<String, String>> getProviderTreeList(String agentId, String setId);

	/**
	 * 批量删除代理人
	 * 
	 * @param ids
	 *            字符串,分割
	 * @return
	 */
//	public String deleteAgentUserBatch(String ids);

	/**
	 * 查询代理人信息
	 * 
	 * @param 
	 * @return
	 */
//	public String getAgentUserInfo(Map<String, Object> map);
	
	/**
	 * 通过代理人id查询代理人工号
	 * 
	 * @param
	 * @return
	 */
//	public String getAgentUserJobNumber(String id);
	
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
//	public Map<String, String> getCertificationPhotoPath(String jobNum);
	/**
	 * 获取所有未认证代理人
	 * @return
	 */
//	public List<Map<String,Object>> getNotCertificationAgentUser();
	/**
	 * 检测当前code是否重复
	 * @param agentusercode
	 * @return
	 */
//	public int selectcountByAgentCode(String agentusercode, String id);
	/**
	 * @param certificationVo
	 * @return
	 */
//	public boolean getOnlyAgentUser(CertificationVo certificationVo);
//	public INSBAgent getAgentUserInfo(INSBAgent temp);
	/**
	 * 批量重置密码
	 * @param ids
	 * @return
	 */
//	public Map<String, String> updateResetPwd(String ids);
	/**
	 * 处理跟踪
	 * @param paramMap
	 * @param taskstatus
	 * @return
	 */
//	public String getJSONOfCarTaskListByMap(Map<String, Object> paramMap,
//											String taskstatus);
	/**
	 * 查询某节点下所有网点id集合
	 * @param id
	 * @return
	 */
//	public List<String> queryWDidsByPatId(String id, String comtype);
	
	/**
	 * 获得代理人
	 * @param jobnum
	 * @return
	 */
//	public INSBAgent getAgentUser(String jobnum);
	
	/**
	 * 获得代理人数据
	 * @return
	 */
//	public Map<String,Object> getAgentUserData(String operator);
	
	/**获取证件类型和行驶区域
	 * @return
	 */
//	public Map<String, Object> getInsuranceInfo();
	
	/**
	 * 判断代理人手机号唯一性
	 * @param phone
	 * @return
	 */
//	public int phoneverify(String phone, String agentiserid);
	
	/**
	 * 
	 */
//	public void updatePwdById();
	
	/**
	 * 获取代理人所在地区
	 * @param agentusernum
	 */
//	public String getAgentUserProvince(String agentusernum);
	
	/**
	 * 生成临时工号
	 * 
	 * @return
	 */
//	public  int updateAgentUserTempJobNo();
	
//	/**
//	 * @param agentUserId 代理人id
//	 * @param jobNo 代理人工号
//	 * @return
//	 */
//	public Map<String,Object> changeAgentPwd(String param);
	
//	public Map<String, Object> getAgentUserData(String operator, String agentusercode);
	
//	public Map<String, Object> setAgentUserAttribute(String parentcodes, String deptid);


	public INSBAgent saveOrUpdateAgentWeiXin(INSBAgent agent);


	/**
	 * 绑定
	 * @param agent
	 */
	public int bindingAgentIdentity(INSBAgent agent);

	/**
	 *
	 * 审核代理人身份信息
	 *
	 * @param user 操作用户
	 * @param agent 代理人信息
	 * @return
	 */
	public String auditAgentIdentity(INSCUser user,INSBAgent agent);

	/**
	 * 按微信openid查询用户信息
	 * @param openid
	 * @return
	 */
	public INSBAgent selectByOpenid(String openid);


	/**
	 * 用户身份验证接口（检查用户提交的认证信息：openid 是否有绑定 手机号）
	 * @param agent 代理人信息
	 * @return
	 */
	public INSBAgent validateAgentIdentity(INSBAgent agent);

	/**
	 * 查询手机号是否 已经是 体系内代理人绑定手机
	 * true是，false不是
	 */
	public boolean checkIsAgentPhone(String phone);

	/**
	 * 查询手机号是否 已经是 minizzb用户绑定手机
	 * true是，false不是
	 */
	public boolean checkIsMinizzbUserPhone(String phone);

	/**
	 * 被推荐人 列表
	 * @param referrerid 推荐人uuid
	 */
	public List<Map<String,Object>> presenteeList(String referrerid);
}
