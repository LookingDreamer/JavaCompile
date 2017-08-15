package com.lzgapi.yzt.bean;

public class CanBindUserValidateBean {
	/**
	 * 验证成功的用户ID
	 */
	private String userid;
	
	/**
	 * 响应状态：OK -成功；FAILED -失败
	 */
	private String status;
	
	/**
	 * 响应结果说明
	 */
	private String message;
	
	/**
	 * 第三方用户姓名
	 */
	private String username;
	
	/**
	 * 是否是代理人
	 */
	private String ismanager;
	
	/**
	 * 代理人工号
	 */
	private String agentcode;
	
	/**
	 * 身份证号
	 */
	private String idno;
	
	/**
	 * 机构编码
	 */
	private String organization;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getIsmanager() {
		return ismanager;
	}

	public void setIsmanager(String ismanager) {
		this.ismanager = ismanager;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
