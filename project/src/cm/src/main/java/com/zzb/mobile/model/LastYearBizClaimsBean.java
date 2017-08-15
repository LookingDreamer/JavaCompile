package com.zzb.mobile.model;


public class LastYearBizClaimsBean {
	String insCorpCode;//保险公司代码
	String caseStartTime;//案件开始时间
	String insCorpName;//保险公司名称 
	String caseEndTime;//案件结束时间
	String policyId;//保单号
	double claimAmount; //理赔金额
	
	public double getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(double claimAmount) {
		this.claimAmount = claimAmount;
	}
	public String getInsCorpCode() {
		return insCorpCode;
	}
	public void setInsCorpCode(String insCorpCode) {
		this.insCorpCode = insCorpCode;
	}
	public String getInsCorpName() {
		return insCorpName;
	}
	public void setInsCorpName(String insCorpName) {
		this.insCorpName = insCorpName;
	}
	public String getCaseStartTime() {
		return caseStartTime;
	}
	public void setCaseStartTime(String caseStartTime) {
		this.caseStartTime = caseStartTime;
	}
	public String getCaseEndTime() {
		return caseEndTime;
	}
	public void setCaseEndTime(String caseEndTime) {
		this.caseEndTime = caseEndTime;
	}
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	
}	
