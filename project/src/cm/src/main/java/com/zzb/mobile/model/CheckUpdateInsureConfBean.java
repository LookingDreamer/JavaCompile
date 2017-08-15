package com.zzb.mobile.model;

import java.util.List;

public class CheckUpdateInsureConfBean {

	/**
	 * 规则校验 false 失败 true 成功
	 */
	private boolean flag;
	/**
	 * 规则校验失败
	 */
	private String reason;
	/**
	 * 有问题的险别
	 */
	private List<String> riskinfos;
	/**
	 * 保险公司名称简称
	 */
	private String prvshotname;
	/**
	 * 保险公司名称
	 */
	private String prvname;
	/**
	 * logo
	 */
	private String logo;
	/**
	 * 保险公司id
	 */
	private String id;
	
	public String getPrvshotname() {
		return prvshotname;
	}

	public void setPrvshotname(String prvshotname) {
		this.prvshotname = prvshotname;
	}

	public String getPrvname() {
		return prvname;
	}

	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<String> getRiskinfos() {
		return riskinfos;
	}

	public void setRiskinfos(List<String> riskinfos) {
		this.riskinfos = riskinfos;
	}

}
