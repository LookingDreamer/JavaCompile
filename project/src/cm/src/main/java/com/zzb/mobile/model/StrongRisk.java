package com.zzb.mobile.model;
/**
 * 交强险 strongRisk
 * @author hejie
 *
 */
public class StrongRisk {

	/**
	 * 不计免赔险别编码
	 */
	private String code;
	/**
	 * 不计免赔险别名称
	 */
	private String name;
	/**
	 * 选中的值
	 */
	private String selected;
	
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
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
	
	
}
