package com.zzb.mobile.model;

import java.util.List;

public class WorkflowStartQuoteModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 供应商id列表
	 */
	private List<String> pids;
	/**
	 * 0  正常投保  1快速续保
	 */
	private String flag;
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public List<String> getPids() {
		return pids;
	}

	public void setPids(List<String> pids) {
		this.pids = pids;
	}
	
	
}
