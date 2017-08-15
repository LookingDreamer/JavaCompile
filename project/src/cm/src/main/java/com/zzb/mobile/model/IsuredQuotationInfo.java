package com.zzb.mobile.model;

import java.util.List;

public class IsuredQuotationInfo {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 报价公司列表
	 */
	private List<String> inscomlist;
	
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public List<String> getInscomlist() {
		return inscomlist;
	}
	public void setInscomlist(List<String> inscomlist) {
		this.inscomlist = inscomlist;
	}
	
}
