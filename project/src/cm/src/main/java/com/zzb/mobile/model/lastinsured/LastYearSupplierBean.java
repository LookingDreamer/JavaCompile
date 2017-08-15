package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;

public class LastYearSupplierBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String  supplierid;//保险公司代码
	String suppliername;//保险公司名称
	public String getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid.trim();
	}
	public String getSuppliername() {
		return suppliername;
	}
	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername.trim();
	}
	
}
