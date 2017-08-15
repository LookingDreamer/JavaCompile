package com.lzgapi.yzt.model;

public class CanBindUserValidateModel {
	/**
	 * 第三方系统用户主键
	 */
	private String userid;

	/**
	 * 一账通id
	 */
	private String managerid;

	/**
	 * 第三方系统用户名
	 */
	private String account;

	/**
	 * 第三方系统用户密码
	 */
	private String password;

	/**
	 * 懒掌柜入口标记位
	 */
	private String token;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "CanBindUserValidateModel [userid=" + userid + ", managerid="
				+ managerid + ", account=" + account + ", password=" + password
				+ ", token=" + token + "]";
	}

}
