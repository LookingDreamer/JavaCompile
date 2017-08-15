package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PersonInfo {
	
	/**
	 * 姓名
	 */
	@XStreamAlias("name")
	private String name;
	/**
	 * 证件类型
	 */
	@XStreamAlias("certificateType")
	private String certificateType;
	/**
	 * 证件号码
	 */
	@XStreamAlias("certNumber")
	private String certNumber;
	/**
	 * 号码
	 */
	@XStreamAlias("tel")
	private String tel;
	/**
	 * 邮箱
	 */
	@XStreamAlias("email")
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertNumber() {
		return certNumber;
	}
	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
