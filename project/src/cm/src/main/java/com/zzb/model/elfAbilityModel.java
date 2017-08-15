package com.zzb.model;



public class elfAbilityModel {
	
	/**
	 * 自动化配置id
	 */
	private String confid;
	/**
	 * 供应商id
	 */
	private String providerid;
	/**
	 * 机构id
	 */
	private String deptid;
	/**
	 * 供应商名称
	 */
	private String providername;
	
	/**
	 * 机构名称
	 */
	private String deptname;
	/**
	 * 能力  01：报价配置 :02：核保配置:03：续保配置.04:承保配置
	 */
	private String ability;
	

	public String getProvidername() {
		return providername;
	}

	public void setProvidername(String providername) {
		this.providername = providername;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public String getConfid() {
		return confid;
	}

	public void setConfid(String confid) {
		this.confid = confid;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	
	
}
