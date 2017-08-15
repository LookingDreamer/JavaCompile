package com.zzb.mobile.model;

public class BindJobNumParam {
	private String JobNumOrIdCard;
	private String jobNumPassword;
	private String tempJobNum;
	
	public String getTempJobNum() {
		return tempJobNum;
	}
	public void setTempJobNum(String tempJobNum) {
		this.tempJobNum = tempJobNum;
	}
	public String getJobNumOrIdCard() {
		return JobNumOrIdCard;
	}
	public void setJobNumOrIdCard(String jobNumOrIdCard) {
		JobNumOrIdCard = jobNumOrIdCard;
	}
	public String getJobNumPassword() {
		return jobNumPassword;
	}
	public void setJobNumPassword(String jobNumPassword) {
		this.jobNumPassword = jobNumPassword;
	}
	
}
