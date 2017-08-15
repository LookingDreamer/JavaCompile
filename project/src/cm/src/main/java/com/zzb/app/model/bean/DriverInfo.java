package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("row")
public class DriverInfo {
	
	/**
	 * 姓名
	 */
	@XStreamAlias("name")
	private String name;
	/**
	 * 性别， 1男，2女
	 */
	@XStreamAlias("gender")
	private String gender;
	/**
	 * 生日
	 */
	@XStreamAlias("birthday")
	private String birthday;
	/**
	 * 驾照类型 C1,C2等
	 */
	@XStreamAlias("driverTypeCode")
	private String driverTypeCode;
	/**
	 * 驾照号
	 */
	@XStreamAlias("licenseNo")
	private String licenseNo;
	/**
	 * 发照日期
	 */
	@XStreamAlias("licensedDate")
	private String licensedDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDriverTypeCode() {
		return driverTypeCode;
	}
	public void setDriverTypeCode(String driverTypeCode) {
		this.driverTypeCode = driverTypeCode;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getLicensedDate() {
		return licensedDate;
	}
	public void setLicensedDate(String licensedDate) {
		this.licensedDate = licensedDate;
	}
	
}
