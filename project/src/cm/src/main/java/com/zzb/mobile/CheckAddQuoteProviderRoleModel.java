package com.zzb.mobile;

import java.util.List;

public class CheckAddQuoteProviderRoleModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 新增供应商id列表
	 */
	private List<String> newproids;
	/**
	 * 阻断规则没通过的供应商id列表
	 */
	private List<String> needdels;
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public List<String> getNewproids() {
		return newproids;
	}
	public void setNewproids(List<String> newproids) {
		this.newproids = newproids;
	}
	public List<String> getNeeddels() {
		return needdels;
	}
	public void setNeeddels(List<String> needdels) {
		this.needdels = needdels;
	}
	
}
