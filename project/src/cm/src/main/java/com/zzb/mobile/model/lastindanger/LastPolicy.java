package com.zzb.mobile.model.lastindanger;


public class LastPolicy {
   //保险公司ID
   String insCorpId;
   //保险公司代码
   String insCorpCode;
   //保险公司名称
   String insCorpName;
   //起保时间
   String policyStartTime;
   //终保时间
   String policyEndTime;
   //保单号
   String policyId;
public String getPolicyId() {
	return policyId;
}
public void setPolicyId(String policyId) {
	this.policyId = policyId;
}
public String getInsCorpName() {
	return insCorpName;
}
public void setInsCorpName(String insCorpName) {
	this.insCorpName = insCorpName;
}
public String getPolicyEndTime() {
	return policyEndTime;
}
public void setPolicyEndTime(String policyEndTime) {
	this.policyEndTime = policyEndTime;
}
public String getPolicyStartTime() {
	return policyStartTime;
}
public void setPolicyStartTime(String policyStartTime) {
	this.policyStartTime = policyStartTime;
}
public String getInsCorpId() {
	return insCorpId;
}
public void setInsCorpId(String insCorpId) {
	this.insCorpId = insCorpId;
}
public String getInsCorpCode() {
	return insCorpCode;
}
public void setInsCorpCode(String insCorpCode) {
	this.insCorpCode = insCorpCode;
}
   
}
