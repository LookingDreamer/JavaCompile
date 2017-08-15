package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 修改权益索赔人接口使用
 */

public class EditLegalrightclaimParam {

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
	 * 修改后的权益索赔人数据json
	 */
	private String legalrightclaimInfoJSON;
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
	public String getLegalrightclaimInfoJSON() {
		return legalrightclaimInfoJSON;
	}
	public void setLegalrightclaimInfoJSON(String legalrightclaimInfoJSON) {
		this.legalrightclaimInfoJSON = legalrightclaimInfoJSON;
	}
	public void setSameWithInsured(boolean isSameWithInsured) {
		this.isSameWithInsured = isSameWithInsured;
	}
	public boolean getIsSameWithInsured() {
		return isSameWithInsured;
	}
	public void setIsSameWithInsured(boolean isSameWithInsured) {
		this.isSameWithInsured = isSameWithInsured;
	}
	
}
