package com.zzb.mobile.model.policyoperat;

/**
 * liu
 * 修改被保人信息接口使用
 */

public class EditInsuredParam {

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
	 * 修改后的被保人数据json
	 */
	private String insuredInfoJSON;
	
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
	public String getInsuredInfoJSON() {
		return insuredInfoJSON;
	}
	public void setInsuredInfoJSON(String insuredInfoJSON) {
		this.insuredInfoJSON = insuredInfoJSON;
	}
	
}
