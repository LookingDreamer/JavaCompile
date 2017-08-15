package com.lzgapi.yzt.model;

public class AgentLzgModel {

	//id
	private String otheruserid;
	
	//掌柜状态：0-不是 1-是
	private String ismanager;
	
	//电话
	private String account;
	
	//jobnum
	private String username;
	
	//代理人工号
	private String agentcode;
	
	//身份证号
	private String idno;
	
	//机构编码
	private String organization;

	public String getOtheruserid() {
		return otheruserid;
	}

	public void setOtheruserid(String otheruserid) {
		this.otheruserid = otheruserid;
	}

	public String getIsmanager() {
		return ismanager;
	}

	public void setIsmanager(String ismanager) {
		this.ismanager = ismanager;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	@Override
	public String toString() {
		return "AgentLzgModel [otheruserid=" + otheruserid + ", ismanager=" + ismanager + ", account=" + account
				+ ", username=" + username + ", agentcode=" + agentcode + ", idno=" + idno + ", organization="
				+ organization + "]";
	}
	
}
