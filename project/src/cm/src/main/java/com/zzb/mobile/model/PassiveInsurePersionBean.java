package com.zzb.mobile.model;

/**
 * @author qiu
 * 被保人
 */
public class PassiveInsurePersionBean {
	private String flag; //1被保人 0投保人 2与被保人一致
    private String name; //姓名
    private String certificateType; //证件类型
    private String certNumber; //证件号码
     
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
