package com.zzb.mobile.model;

/**
 * @author qiu
 * 车主
 */
public class CarowerinfoBean {
	private String name;
    private String certificateType; //证件类型
    private String certNumber; //证件号
    private String flag;    //与被保人一致
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
    
}
