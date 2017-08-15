package com.zzb.mobile.model;

public class OrderTrackingParam {
	/*
	 * 主流程id
	 */
	private String processInstanceId;
	/*
	 * 保险公司id
	 */
	private String prvCode;
	/*
	 * 子流程id
	 */
	private String subInstanceId;
	
	OrderTrackingParam(){}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getPrvCode() {
		return prvCode;
	}

	public void setPrvCode(String prvCode) {
		this.prvCode = prvCode;
	}

	public String getSubInstanceId() {
		return subInstanceId;
	}

	public void setSubInstanceId(String subInstanceId) {
		this.subInstanceId = subInstanceId;
	}
	
}
