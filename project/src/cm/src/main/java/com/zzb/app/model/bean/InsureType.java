package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("row")
public class InsureType {

	/**
	 * 险种代码
	 */
	@XStreamAlias("code")
	private String code;
	/**
	 * 险种名称
	 */
	@XStreamAlias("name")
	private String name;
	/**
	 * 保额
	 */
	@XStreamAlias("coverage")
	private String coverage;
	/**
	 * 当前选择的选项
	 */
	@XStreamAlias("selectedOption")
	private String selectedOption;
	/**
	 * 单位   万/座
	 */
	@XStreamAlias("unit")
	private String unit;
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	
}
