package com.zzb.mobile.model.lastyear;

import java.io.Serializable;

public class LastYearRiskinfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	private String kindname;
	/**
	 * 编码
	 */
	private String kindcode;
	/**
	 * 保费
	 */
	private Double prem;
	/**
	 * 保额
	 */
	private Double amount;
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

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

}
