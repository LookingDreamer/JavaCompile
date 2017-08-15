package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;

public class LastYearRiskinfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String kindname;//名称，
	String kindcode;//编码，
	Double prem;//保费
	Double amount;//保额
	public String getKindname() {
		return kindname;
	}
	public void setKindname(String kindname) {
		this.kindname = kindname;
	}
	public String getKindcode() {
		return kindcode;
	}
	public void setKindcode(String kindcode) {
		this.kindcode = kindcode;
	}
	public Double getPrem() {
		return prem;
	}
	public void setPrem(Double prem) {
		this.prem = prem;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	

}
