package com.zzb.mobile.model.lastindanger;

/**
 * 事故信息
 * @author CAOBIN
 *
 */
public class BizClaims {


	//保险公司ID
    String insCorpId;

    //保险公司代码
    String insCorpCode;

    //保险公司名称
    String insCorpName;

    //出险时间
    String caseStartTime;

    //结案时间
    String caseEndTime;

    //理赔金额
    double claimAmount;

    //保单号
    String policyId;
    
    

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

	public double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

  
}
