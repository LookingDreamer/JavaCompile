package com.zzb.mobile.model;

public class StrongInsuranceBean {

	/**
	 * 险别名称
	 */
	private String riskname;
	/**
	 * 保费
	 */
	private Double countcharge;
	/**
	 * 保额  投保
	 */
	private String amount;
	
	public String getRiskname() {
		return riskname;
	}
	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}
	public Double getCountcharge() {
		return countcharge;
	}
	public void setCountcharge(Double countcharge) {
		this.countcharge = countcharge;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
