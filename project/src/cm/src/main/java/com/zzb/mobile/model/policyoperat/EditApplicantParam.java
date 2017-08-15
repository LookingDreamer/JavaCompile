package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 修改投保人信息接口使用
 */

public class EditApplicantParam {

	/**
	 * 流程实例id
	 */
	private String processInstanceId;
	/**
	 * 保险公司code
	 */
	private String inscomcode;
	/**
	 * 操作员姓名
	 */
	private String operator;
	/**
	 * 修改后的投保人数据json
	 */
	private String applicantInfoJSON;
	/**
	 * 是否和被保人一致标记
	 */
	private boolean isSameWithInsured;
	
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getApplicantInfoJSON() {
		return applicantInfoJSON;
	}
	public void setApplicantInfoJSON(String applicantInfoJSON) {
		this.applicantInfoJSON = applicantInfoJSON;
	}
	public boolean getIsSameWithInsured() {
		return isSameWithInsured;
	}
	public void setIsSameWithInsured(boolean isSameWithInsured) {
		this.isSameWithInsured = isSameWithInsured;
	}
	
}
