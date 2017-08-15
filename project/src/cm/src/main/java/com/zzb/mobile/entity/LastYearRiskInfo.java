package com.zzb.mobile.entity;

import java.util.Map;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class LastYearRiskInfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 输入的参数
	 */
	private Map<String,String> inputData;
	/**
	 * 代理人id
	 */
	private String agentId;
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
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
}
