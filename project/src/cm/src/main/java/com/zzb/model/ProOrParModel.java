package com.zzb.model;

public class ProOrParModel {
	
	/**
	 * 上级编码
	 */
	private String parentcode;
	/**
	 * 供应商合作商编码
	 */
	private String prvcode;	
	/**
	 * 供应商合作商名称
	 */
	private String prvname;
	/**
	 * 供应商合作商层级
	 */
	private String prvgrade;
	
	/**
	 * 供应商合作商类型
	 */
	private String businesstype;
	
	/**
	 * 归属机构
	 */
	private String affiliationorg;
	
	

	public String getParentcode() {
		return parentcode;
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public String getPrvcode() {
		return prvcode;
	}

	public void setPrvcode(String prvcode) {
		this.prvcode = prvcode;
	}

	public String getPrvname() {
		return prvname;
	}

	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public String getPrvgrade() {
		return prvgrade;
	}

	public void setPrvgrade(String prvgrade) {
		this.prvgrade = prvgrade;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getAffiliationorg() {
		return affiliationorg;
	}

	public void setAffiliationorg(String affiliationorg) {
		this.affiliationorg = affiliationorg;
	}
	
	
}
