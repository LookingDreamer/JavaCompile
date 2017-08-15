package com.lzgapi.yzt.bean;

public class UptoManagerBean {
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
	 * 用户ID
	 */
	private String otheruserid;
	
	/**
	 * 代理人工号
	 */
	private String agentcode;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 身份证号
	 */
	private String idno;
	
	/**
	 * 机构编码
	 */
	private String organization;
	
	/**
	 * 平台编号
	 */
	private String platform;
	
	/**
	 * token
	 */
	private String token;

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

	public String getOtheruserid() {
		return otheruserid;
	}

	public void setOtheruserid(String otheruserid) {
		this.otheruserid = otheruserid;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
