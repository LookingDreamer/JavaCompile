package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBPrvaccountmanager extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * 保险公司内部机构
	 */
	private String org;

	/**
	 * cm保险公司id
	 */
	private String providerid;
	

	/**
	 * 账号
	 */
	private String account;

	/**
	 * 出单网点
	 */
	private String deptid;
	
	/**
	 * 出单网点
	 */
	private String deptname;
	

	/**
	 * 版本
	 */
	private String version;

	/**
	 * 密码
	 */
	private String pwd;

	/**
	 * 参数名称
	 */
	private String permission;

	/**
	 * 登陆地址
	 */
	private String loginurl;

	/**
	 * 用途
	 */
	private String usetype;
	
	/**
	 * 用途name
	 */
	private String usetypeStr;

	/**
	 * 供应商名称
	 */
	private String prvname;

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getLoginurl() {
		return loginurl;
	}

	public void setLoginurl(String loginurl) {
		this.loginurl = loginurl;
	}

	public String getUsetype() {
		return usetype;
	}

	public void setUsetype(String usetype) {
		this.usetype = usetype;
	}

	public String getPrvname() {
		return prvname;
	}

	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	
	

	public String getUsetypeStr() {
		return usetypeStr;
	}

	public void setUsetypeStr(String usetypeStr) {
		this.usetypeStr = usetypeStr;
	}

	@Override
	public String toString() {
		return "INSBPrvaccountmanager [org=" + org + ", providerid="
				+ providerid + ", account=" + account + ", deptid=" + deptid
				+ ", version=" + version + ", pwd=" + pwd + ", permission="
				+ permission + ", loginurl=" + loginurl + ", usetype="
				+ usetype + ", usetypeStr=" + usetypeStr + ", prvname="
				+ prvname + "]";
	}
	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	

}