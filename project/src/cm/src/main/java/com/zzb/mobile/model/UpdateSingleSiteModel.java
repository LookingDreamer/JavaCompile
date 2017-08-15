package com.zzb.mobile.model;


public class UpdateSingleSiteModel {
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 供应商id
	 */
	private String pid;
	/**
	 * 出单网点id
	 */
	private String siteid;
	
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
}
