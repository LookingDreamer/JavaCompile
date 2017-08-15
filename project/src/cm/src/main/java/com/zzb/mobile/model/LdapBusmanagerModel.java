package com.zzb.mobile.model;

public class LdapBusmanagerModel {
	private String cn;//账号
	private String sn;//姓
	private String userPassword; //密码
	private String description="test";
	private String DN;  //DN
	private String parentDN;
	private String objectClass="inetOrgPerson"; //
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDN() {
		return DN;
	}
	public void setDN(String dN) {
		DN = dN;
	}
	public String getParentDN() {
		return parentDN;
	}
	public void setParentDN(String parentDN) {
		this.parentDN = parentDN;
	}
	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	
	@Override
	public String toString() {
		return "LdapBusmanagerModel [cn=" + cn + ", sn=" + sn
				+ ", userPassword=" + userPassword + ", description="
				+ description + ", DN=" + DN + ", parentDN=" + parentDN
				+ ", objectClass=" + objectClass + "]";
	}
}
