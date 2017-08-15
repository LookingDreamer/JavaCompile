package com.zzb.mobile.model;

import java.util.List;

public class ChoiceProviderIdsModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 传递的参数  协议id#供应商id#出单网点id#网销传统类型
	 * 必需参数，一一对应
	 */
	private List<String> paramsList;
	/**
	 * 01传统02网销03电销
	 */
	private String buybusitype;
	/**
	 * 投保书标记
	 */
	private String webpagekey;
	/**
	 * 平台查询状态 true 1 false 0
	 */
	private boolean cloudstate;
	/**
	 * 手工选择的投保公司名称
	 */
	private String handersupplier;
	/**
	 * 查询到的上年投保公司名称
	 */
	private String lastyearsupplier;
	
	public boolean isCloudstate() {
		return cloudstate;
	}
	public void setCloudstate(boolean cloudstate) {
		this.cloudstate = cloudstate;
	}
	public String getHandersupplier() {
		return handersupplier;
	}
	public void setHandersupplier(String handersupplier) {
		this.handersupplier = handersupplier;
	}
	public String getLastyearsupplier() {
		return lastyearsupplier;
	}
	public void setLastyearsupplier(String lastyearsupplier) {
		this.lastyearsupplier = lastyearsupplier;
	}
	public String getWebpagekey() {
		return webpagekey;
	}
	public void setWebpagekey(String webpagekey) {
		this.webpagekey = webpagekey;
	}
	public String getBuybusitype() {
		return buybusitype;
	}
	public void setBuybusitype(String buybusitype) {
		this.buybusitype = buybusitype;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public List<String> getParamsList() {
		return paramsList;
	}
	public void setParamsList(List<String> paramsList) {
		this.paramsList = paramsList;
	}
	
}
