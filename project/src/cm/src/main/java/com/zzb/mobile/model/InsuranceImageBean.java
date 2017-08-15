package com.zzb.mobile.model;

public class InsuranceImageBean {

	/**
	 * 影像件名称
	 */
	private String riskimgname;
	/**
	 * 影像类型
	 */
	private String riskimgtype;
	/**
	 * 影像类型名称
	 */
	private String riskimgtypename;
	/**
	 * 对应字典表codetype
	 */
	private String codetype;
	
	public String getCodetype() {
		return codetype;
	}
	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}
	public String getRiskimgname() {
		return riskimgname;
	}
	public void setRiskimgname(String riskimgname) {
		this.riskimgname = riskimgname;
	}
	public String getRiskimgtype() {
		return riskimgtype;
	}
	public void setRiskimgtype(String riskimgtype) {
		this.riskimgtype = riskimgtype;
	}
	public String getRiskimgtypename() {
		return riskimgtypename;
	}
	public void setRiskimgtypename(String riskimgtypename) {
		this.riskimgtypename = riskimgtypename;
	}
	
	
}
