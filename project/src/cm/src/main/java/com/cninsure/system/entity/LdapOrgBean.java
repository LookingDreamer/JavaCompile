package com.cninsure.system.entity;

/**
 * @author Andy
 * @date 13:43 2015/11/21
 * @version 1.0
 */
public class LdapOrgBean {
	private String businessCategory;// 机构编码
	private String destinationIndicator;// 机构编码
	private String description;// 机构名称
	private String physicalDeliveryOfficeName;// 短名称
	private String objectClass;
	private String parentDN;
	private String telexNumber;// 机构级别
	private String st;// 所在省
	private String l;// 所在市
	private String o;
	private String ou;
	private String postalAddress;// 机构地址
	private String postalCode;// 机构邮编
	private String cn;
	private String DN;
	private String successFlag = "false";
	private String telephoneNumber;// 机构电话
	private String facsimileTelephoneNumber;// 机构传真
	private String street;// 机构负责人姓名
	private String postOfficeBox;// Email
	private String registeredAddress;// 网址

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
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

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

	public String getDestinationIndicator() {
		return destinationIndicator;
	}

	public void setDestinationIndicator(String destinationIndicator) {
		this.destinationIndicator = destinationIndicator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhysicalDeliveryOfficeName() {
		return physicalDeliveryOfficeName;
	}

	public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
		this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
	}

	public String getTelexNumber() {
		return telexNumber;
	}

	public void setTelexNumber(String telexNumber) {
		this.telexNumber = telexNumber;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getFacsimileTelephoneNumber() {
		return facsimileTelephoneNumber;
	}

	public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
		this.facsimileTelephoneNumber = facsimileTelephoneNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostOfficeBox() {
		return postOfficeBox;
	}

	public void setPostOfficeBox(String postOfficeBox) {
		this.postOfficeBox = postOfficeBox;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	@Override
	public String toString() {
		return "LdapOrgBean [o=" + o + ", ou=" + ou + ", businessCategory="
				+ businessCategory + ", destinationIndicator="
				+ destinationIndicator + ", description=" + description
				+ ", physicalDeliveryOfficeName=" + physicalDeliveryOfficeName
				+ ", objectClass=" + objectClass + ", cn=" + cn + ", DN=" + DN
				+ ", successFlag=" + successFlag + ", parentDN=" + parentDN
				+ ", telexNumber=" + telexNumber + ", st=" + st + ", l=" + l
				+ ", postalAddress=" + postalAddress + ", postalCode="
				+ postalCode + ", telephoneNumber=" + telephoneNumber
				+ ", facsimileTelephoneNumber=" + facsimileTelephoneNumber
				+ ", street=" + street + ", postOfficeBox=" + postOfficeBox
				+ ", registeredAddress=" + registeredAddress + "]";
	}

}
