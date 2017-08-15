package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DriverBean extends PersonBean {
	private String driverLicenseType;
	private String driverLicenseDesc;
	private String driverLicenseNo;
	private String driverLicenseState;
	private String firstIssueDate;
	private String isPrimary;

	public String getDriverLicenseType() {
		return driverLicenseType;
	}

	public void setDriverLicenseType(String driverLicenseType) {
		this.driverLicenseType = driverLicenseType;
	}

	public String getDriverLicenseDesc() {
		return driverLicenseDesc;
	}

	public void setDriverLicenseDesc(String driverLicenseDesc) {
		this.driverLicenseDesc = driverLicenseDesc;
	}

	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}

	public String getDriverLicenseState() {
		return driverLicenseState;
	}

	public void setDriverLicenseState(String driverLicenseState) {
		this.driverLicenseState = driverLicenseState;
	}

	public String getFirstIssueDate() {
		return firstIssueDate;
	}

	public void setFirstIssueDate(String firstIssueDate) {
		this.firstIssueDate = firstIssueDate;
	}

	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

}
