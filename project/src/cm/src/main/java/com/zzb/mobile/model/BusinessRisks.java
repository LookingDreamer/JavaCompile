package com.zzb.mobile.model;
/**
 * 商业险 businessRisks
 * @author hejie
 *
 */
public class BusinessRisks {

	/**
	 * 险别编码
	 */
	private String code;
	/**
	 * 险别名称
	 */
	private String name;
	/**
	 * 保额
	 */
	private String coverage;
	/**
	 * 下拉列表选中的key值
	 */
	private String selectedOption;
	/**
	 * 单位 如 万/座
	 */
	private String unit;
	/**
	 * 0 不计免赔 1不是
	 */
	private String flag;
	/**
	 * 是否是玻璃   01不是  02是
	 */
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public String getSelectedOption() {
		return selectedOption;
	}
	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
