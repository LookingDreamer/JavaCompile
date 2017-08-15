package com.zzb.mobile.model;

public class LoginPram {
	private String account;
	private String password;
	private String openid;
	private String lzgid;
	private String requirementid;
	private String clienttype;
	private String usersource;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getLzgid() {
		return lzgid;
	}
	public void setLzgid(String lzgid) {
		this.lzgid = lzgid;
	}
	public String getRequirementid() {
		return requirementid;
	}
	public void setRequirementid(String requirementid) {
		this.requirementid = requirementid;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public String getUsersource() {
		return usersource;
	}

	public void setUsersource(String usersource) {
		this.usersource = usersource;
	}
}
