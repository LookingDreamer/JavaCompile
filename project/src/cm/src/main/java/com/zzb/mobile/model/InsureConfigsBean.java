package com.zzb.mobile.model;



public class InsureConfigsBean {

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
	 * 是否是不计免赔  1 是的 
	 */
	private String isdeductible;
	/**
	 * 险别所需数据项 存json对象
	 */
	private InsuredConfig insuredConfig;
	/**
	 * 选中的下拉项key值  保额的值
	 */
	private String coverage;
	/**
	 * 是否选中险种
	 */
	private boolean isSelect;
	/**
	 * 是否选中不计免赔
	 */
	private boolean flag;
	
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public boolean isIsSelect() {
		return isSelect;
	}
	public void setIsSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
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
	public InsuredConfig getInsuredConfig() {
		return insuredConfig;
	}
	public void setInsuredConfig(InsuredConfig insuredConfig) {
		this.insuredConfig = insuredConfig;
	}
	
}
