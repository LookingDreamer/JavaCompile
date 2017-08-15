package com.lzgapi.yzt.bean;

public class RegisterOrBecomeAgentBean {
	/**
	 * 响应状态：OK -成功；FAILED -失败
	 */
	private String status;

	/**
	 * 第三方平台用户id
	 */
	private String userid;

	/**
	 * 响应结果说明
	 */
	private String message;

	/**
	 * 第三方用户姓名
	 */
	private String username;

	/**
	 * 第三方账号
	 */
	private String account;

	/**
	 * 是否是代理人(1-是,0-不是)
	 */
	private String ismanager;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIsmanager() {
		return ismanager;
	}

	public void setIsmanager(String ismanager) {
		this.ismanager = ismanager;
	}

}
