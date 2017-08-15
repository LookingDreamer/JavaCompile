package com.zzb.mobile.model;
/**
 * 补充信息
 * @author hejie
 *
 */
public class SupplementaryinfoBean {

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
	 * 报价必录 1必录
	 */
	private String isquotemust;
	/**
	 * 录入方式
	 */
	private String inputtype;
	/**
	 * 可选内容
	 */
	private Object optional;
	
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
	public String getIsquotemust() {
		return isquotemust;
	}
	public void setIsquotemust(String isquotemust) {
		this.isquotemust = isquotemust;
	}
	public String getInputtype() {
		return inputtype;
	}
	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}
	public Object getOptional() {
		return optional;
	}
	public void setOptional(Object optional) {
		this.optional = optional;
	}
	
}
