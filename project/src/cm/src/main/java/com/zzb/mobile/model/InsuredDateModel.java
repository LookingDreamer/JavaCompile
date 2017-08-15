package com.zzb.mobile.model;

public class InsuredDateModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 0 商业险 1 交强险
	 */
	private String type;
	/**
	 * 开始时间
	 */
	private String riskstartdate;
	/**
	 * 结束时间
	 */
	private String riskenddate;
	
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRiskstartdate() {
		return riskstartdate;
	}
	public void setRiskstartdate(String riskstartdate) {
		this.riskstartdate = riskstartdate;
	}
	public String getRiskenddate() {
		return riskenddate;
	}
	public void setRiskenddate(String riskenddate) {
		this.riskenddate = riskenddate;
	}
	
}
