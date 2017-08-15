package com.zzb.app.model.bean;

public class TaskAndRisk {
	private String processInstanceId;
	private String risktype;
	private String prvid;
	
	/**
	 * @return the processInstanceId
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	/**
	 * @param processInstanceId the processInstanceId to set
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	/**
	 * @return the risktype
	 */
	public String getRisktype() {
		return risktype;
	}
	/**
	 * @param risktype the risktype to set
	 */
	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}
	public String getPrvid() {
		return prvid;
	}
	public void setPrvid(String prvid) {
		this.prvid = prvid;
	}
	
}
