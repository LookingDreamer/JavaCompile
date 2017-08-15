package com.zzb.mobile.model;

public class GuiZeCheckBean {

	/**
	 * 供应商id
	 */
	private String pid;
	/**
	 * 供应商名称
	 */
	private String prvname;
	/**
	 * 供应商名称简称
	 */
	private String prvshotname;
	/**
	 * 供应商logo
	 */
	private String logo;
	/**
	 * 规则校验失败原因
	 */
	private String reason;
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPrvname() {
		return prvname;
	}
	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}
	public String getPrvshotname() {
		return prvshotname;
	}
	public void setPrvshotname(String prvshotname) {
		this.prvshotname = prvshotname;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
