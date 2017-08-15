package com.zzb.mobile.model;
/**
 * 驾驶员信息
 * @author hejie
 *
 */
public class CarDriver {
	
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 驾驶证类型
	 */
	private String driverTypeCode;
	/**
	 * 驾驶证编号
	 */
	private String licenseNo;
	/**
	 * 发证日期
	 */
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
