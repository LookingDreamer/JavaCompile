package com.zzb.app.service;

import com.zzb.app.model.AppRegistModel;

public interface AppRegistService {

	/**
	 * 找回密码--发送验证码
	 * 
	 * @param account
	 * @return
	 */
	public String findpws(String account);

	/**
	 * 校验忘记密码验证码(身份证)
	 * 
	 * @param phone
	 * @param code
	 * @param idno
	 * @return
	 */
	public String validatorFindPws(String phone, String code, String idno);

	/**
	 * 修改代理人登录密码
	 * 
	 * @param phone
	 * @param newpwd
	 * @return
	 */
	public String updatepwd(String phone, String newpwd);

	/**
	 * 注册--发送手机验证码 同时手机号重复性校验
	 * 
	 * @param phoneNum
	 * @return
	 */
	public String RegistvalidationCode(String phoneNum);

	/**
	 * 注册--提交注册信息
	 * 
	 * @param registModel
	 * @return
	 */
	public String register(AppRegistModel registModel);

	/**
	 * 注册--绑定工号
	 * @param jobNumOrIdCard
	 * @param jobNumPassword
	 * @return
	 */
	public String BindingJobNum(String jobNumOrIdCard, String jobNumPassword);

	/**
	 * 首页 欢迎姓名
	 * @param token
	 * @return
	 */
	public String getAgentName(String token);

}
