package com.zzb.mobile.model;

public class InsbpaymentpasswordModel {

private String jobNum;

private String logpwd;

private String paypwd;

public String getJobNum() {
	return jobNum;
}

public void setJobNum(String jobNum) {
	this.jobNum = jobNum;
}

public String getLogpwd() {
	return logpwd;
}

public void setLogpwd(String logpwd) {
	this.logpwd = logpwd;
}

public String getPaypwd() {
	return paypwd;
}

public void setPaypwd(String paypwd) {
	this.paypwd = paypwd;
}

@Override
public String toString() {
	return "InsbpaymentpasswordModel [jobNum=" + jobNum + ", logpwd=" + logpwd
			+ ", paypwd=" + paypwd + "]";
}



	
}
