package com.zzb.mobile.model;

public class SupplementInfo {
	/**
	 * 要素名称
	 */
	private String itemname;
	/**
	 * 要素编码
	 */
	private String itemcode;
	/**
	 * 要素类型
	 */
	private String itemtype;
	/**
	 * 录入方式  文本 下拉列表
	 */
	private String inputtype;
	/**
	 *  填写的内容{"key":"value"} 
	 */
	private String optional;
	
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItemtype() {
		return itemtype;
	}
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	public String getInputtype() {
		return inputtype;
	}
	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}
	public String getOptional() {
		return optional;
	}
	public void setOptional(String optional) {
		this.optional = optional;
	}
	
}
