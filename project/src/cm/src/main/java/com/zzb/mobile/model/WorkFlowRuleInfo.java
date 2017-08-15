package com.zzb.mobile.model;

public class WorkFlowRuleInfo {

	/**
	 * 主实例id
	 */
	private String processinstanceid;
	/**
	 * 子流程id
	 */
	private String subinstanceid;
	/**
	 * 供应商id
	 */
	private String inscomcode;
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getSubinstanceid() {
		return subinstanceid;
	}
	public void setSubinstanceid(String subinstanceid) {
		this.subinstanceid = subinstanceid;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	
}
