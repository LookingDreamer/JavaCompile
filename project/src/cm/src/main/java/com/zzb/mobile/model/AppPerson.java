package com.zzb.mobile.model;


/**
 * 被保人/投保人/车主
 * 
 * @author qiu
 *
 */
public class AppPerson {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 证件类型
	 */
	private String certificateType;
	/**
	 * 证件号
	 */
	private String certNumber;
	/**
	 * email
	 */
	private String email;
    /**
     * 电话
     */
    private String tel;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
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
	
	
}
