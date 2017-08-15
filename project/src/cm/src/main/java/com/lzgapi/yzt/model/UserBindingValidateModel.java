package com.lzgapi.yzt.model;

public class UserBindingValidateModel {
	/**
	 * 微信用户id
	 */
	private String openid;

	/**
	 * 手机号
	 */
	private String cellphone;

	/**
	 * 身份证号
	 */
	private String idnumber;

	/**
	 * 第三方系统用户名
	 */
	private String username;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 懒掌柜入口标记位
	 */
	private String token;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserBindingValidateModel [openid=" + openid + ", cellphone="
				+ cellphone + ", idnumber=" + idnumber + ", username="
				+ username + ", email=" + email + ", token=" + token + "]";
	}

}
