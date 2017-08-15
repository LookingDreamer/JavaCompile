package com.zzb.app.model;

public class InsureConfigModel {

	/**
	 * id
	 */
	private String id;
	/**
	 * 方案名称
	 */
	private String planname;
	/**
	 * 方案key
	 */
	private String plankey;
	/**
	 * 类型 0 商业险 1 交强险车船税
	 */
	private String type;
	/**
	 * 险别名称
	 */
	private String riskkindname;
	/**
	 * 险别编码
	 */
	private String riskkindcode;
	/**
	 * 所需的前置险别编码
	 */
	private String prekindcode;
	/**
	 * 是否是不计免赔
	 */
	private String isdeductible;
	/**
	 * 险别所需数据项 存json对象
	 */
	private String datatemp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanname() {
		return planname;
	}
	public void setPlanname(String planname) {
		this.planname = planname;
	}
	public String getPlankey() {
		return plankey;
	}
	public void setPlankey(String plankey) {
		this.plankey = plankey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRiskkindname() {
		return riskkindname;
	}
	public void setRiskkindname(String riskkindname) {
		this.riskkindname = riskkindname;
	}
	public String getRiskkindcode() {
		return riskkindcode;
	}
	public void setRiskkindcode(String riskkindcode) {
		this.riskkindcode = riskkindcode;
	}
	public String getPrekindcode() {
		return prekindcode;
	}
	public void setPrekindcode(String prekindcode) {
		this.prekindcode = prekindcode;
	}
	public String getIsdeductible() {
		return isdeductible;
	}
	public void setIsdeductible(String isdeductible) {
		this.isdeductible = isdeductible;
	}
	public String getDatatemp() {
		return datatemp;
	}
	public void setDatatemp(String datatemp) {
		this.datatemp = datatemp;
	}
	
}
