package com.zzb.mobile.model;

public class LdapAgentModel {
	private String mobile; //手机号
	private String labeledURI="test"; //绑定设备 在ldap上的DN
	private String initials="1"; //性别 男/女
	private String givenName; //名
	private String sn;//姓
	private String userPassword; //密码
	private String displayName; //姓名
	private String objectClass="inetOrgPerson"; //
	private String uid;
	private String registeredAddress="test";
	private String cn;
	private String title="test";
	private String employeeNumber;
	private String description="test";
	private String businessCategory="01"; //证件类别
	private String DN;  //DN
	private String parentDN;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLabeledURI() {
		return labeledURI;
	}
	public void setLabeledURI(String labeledURI) {
		this.labeledURI = labeledURI;
	}
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBusinessCategory() {
		return businessCategory;
	}
	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}
	public String getDN() {
		return DN;
	}
	public void setDN(String dn) {
		this.DN = dn;
	}
	public String getParentDN() {
		return parentDN;
	}
	public void setParentDN(String parentDN) {
		this.parentDN = parentDN;
	}
	@Override
	public String toString() {
		return "LdapAgentModel [mobile=" + mobile + ", labeledURI="
				+ labeledURI + ", initials=" + initials + ", givenName="
				+ givenName + ", sn=" + sn + ", userPassword=" + userPassword
				+ ", displayName=" + displayName + ", objectClass="
				+ objectClass + ", uid=" + uid + ", registeredAddress="
				+ registeredAddress + ", cn=" + cn + ", title=" + title
				+ ", employeeNumber=" + employeeNumber + ", description="
				+ description + ", businessCategory=" + businessCategory
				+ ", DN=" + DN + ", parentDN=" + parentDN + "]";
	}
	
	
	
}
