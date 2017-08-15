package com.zzb.chn.bean;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 核保补充数据项bean
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class InsureSupplyBean {
	private String itemCode;
	private String itemName;
	private String itemInputType;
	private Map itemOptions;
	private String itemValue;
	
	public String getItemInputType() {
		return itemInputType;
	}
	public void setItemInputType(String itemInputType) {
		this.itemInputType = itemInputType;
	}
	public Map getItemOptions() {
		return itemOptions;
	}
	public void setItemOptions(Map itemOptions) {
		this.itemOptions = itemOptions;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	 
}
