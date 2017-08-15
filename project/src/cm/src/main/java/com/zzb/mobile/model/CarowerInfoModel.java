package com.zzb.mobile.model;

public class CarowerInfoModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 证件类型
	 */
	private String certificateType;
	/**
	 * 证件号
	 */
	private String certNumber;
	/**
	 * 证件与被保人一致  0 一致  1不一致
	 */
	private String flag;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
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
