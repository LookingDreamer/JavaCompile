package com.cninsure.system.entity;

/**
 * @author Andy
 * @date 13:43 2015/11/21
 * @version 1.0
 */
public class LdapAgentBean {
	private String employeeNumber;// 工号
	private String sn;// 姓
	private String givenName;// 名
	private String displayName;// 姓名
	private String userPassword;// 密码
	private String objectClass;//
	private String DN;//
	private String successFlag = "false";
	private String parentDN;// parentDN
	private String uid;//
	private String cn;//
	private String mobile;// 手机号码
	private String mail;// 邮箱
	private String initials;// 性别
	private String businessCategory;// 证件类型
	private String registeredAddress;// 证件号码
	private String destinationIndicator;// 是否允许通过任何移动设备登录
	private String labeledURI;// 绑定设备
	private String title;// 用户权限
	private String l;// 禁用/启用移动登录
	private String st;// 禁用/启用网站登录
	private String description;// 最近登录时间
	private String employeeType;// VIP等级
	private String mgmtDivision;// 网点编码
	private String agentGroup;// 团队编码

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public String getDN() {
		return DN;
	}

	public void setDN(String dN) {
		DN = dN;
	}

	public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	public String getParentDN() {
		return parentDN;
	}

	public void setParentDN(String parentDN) {
		this.parentDN = parentDN;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getDestinationIndicator() {
		return destinationIndicator;
	}

	public void setDestinationIndicator(String destinationIndicator) {
		this.destinationIndicator = destinationIndicator;
	}

	public String getLabeledURI() {
		return labeledURI;
	}

	public void setLabeledURI(String labeledURI) {
		this.labeledURI = labeledURI;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getMgmtDivision() {
		return mgmtDivision;
	}

	public void setMgmtDivision(String mgmtDivision) {
		this.mgmtDivision = mgmtDivision;
	}

	public String getAgentGroup() {
		return agentGroup;
	}

	public void setAgentGroup(String agentGroup) {
		this.agentGroup = agentGroup;
	}

	@Override
	public String toString() {
		return "LdapAgentBean [employeeNumber=" + employeeNumber + ", sn=" + sn
				+ ", givenName=" + givenName + ", displayName=" + displayName
				+ ", userPassword=" + userPassword + ", objectClass="
				+ objectClass + ", DN=" + DN + ", successFlag=" + successFlag
				+ ", parentDN=" + parentDN + ", uid=" + uid + ", cn=" + cn
				+ ", mobile=" + mobile + ", mail=" + mail + ", initials="
				+ initials + ", businessCategory=" + businessCategory
				+ ", registeredAddress=" + registeredAddress
				+ ", destinationIndicator=" + destinationIndicator
				+ ", labeledURI=" + labeledURI + ", title=" + title + ", l="
				+ l + ", st=" + st + ", description=" + description
				+ ", employeeType=" + employeeType + ", mgmtDivision="
				+ mgmtDivision + ", agentGroup=" + agentGroup + "]";
	}

}
