package com.zzb.mobile.service;


public interface SMSClientService {
	/**
	 * 发送手机验证码
	 * @param phone
	 * @param code
	 * @throws Exception
	 */
	public void sendMobileValidateCode(String phone, String code) throws Exception;
	
	/**
	 * 发送成功注册掌中保的用户名和密码
	 * @param phone
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void sendRegistSuccessMessage(String phone,String username, String password) throws Exception;

	/**
	 * 发送密码更新成功短信给用户
	 *
	 * @param phone    用户手机号码
	 * @param username 用户账号
	 * @throws Exception
	 */
	public void sendUpdateSuccessMessage(String phone, String username) throws Exception;
}
