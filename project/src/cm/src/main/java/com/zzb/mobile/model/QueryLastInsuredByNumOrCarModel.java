package com.zzb.mobile.model;

import java.util.Map;

public class QueryLastInsuredByNumOrCarModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 输入的参数
	 */
	private Map<String,String> inputData;
	/**
	 * 供应商id
	 */
	private String proid;
	/**
	 * 代理人id
	 */
	private String agentId;
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public Map<String, String> getInputData() {
		return inputData;
	}
	public void setInputData(Map<String, String> inputData) {
		this.inputData = inputData;
	}
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	
	
}
