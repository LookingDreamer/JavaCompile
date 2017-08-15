package com.zzb.mobile.model;

public class UpdateInsureDateModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 供应商id
	 */
	private String inscomcode;
	/**
	 * 商业险起保日期
	 */
	private String systartdate;
	/**
	 * 交强险起保日期
	 */
	private String jqstartdate;
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getInscomcode() {
		return inscomcode;
	}
	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}
	public String getSystartdate() {
		return systartdate;
	}
	public void setSystartdate(String systartdate) {
		this.systartdate = systartdate;
	}
	public String getJqstartdate() {
		return jqstartdate;
	}
	public void setJqstartdate(String jqstartdate) {
		this.jqstartdate = jqstartdate;
	}
	
	
}
