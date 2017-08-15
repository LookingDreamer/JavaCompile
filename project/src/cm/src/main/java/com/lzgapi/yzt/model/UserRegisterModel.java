package com.lzgapi.yzt.model;

import com.zzb.mobile.model.CommonModel;

public class UserRegisterModel extends CommonModel {
	private String userid;
	private String username;
	private String ismanager;
	private String lzgid;
	private String loginame;
	private String password;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getLzgid() {
		return lzgid;
	}
	public void setLzgid(String lzgid) {
		this.lzgid = lzgid;
	}
	public String getLoginame() {
		return loginame;
	}
	public void setLoginame(String loginame) {
		this.loginame = loginame;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
