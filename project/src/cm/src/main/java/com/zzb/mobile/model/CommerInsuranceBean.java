package com.zzb.mobile.model;

public class CommerInsuranceBean {

	/**
	 * 险别名称
	 */
	private String riskname;
	/**
	 * 保费
	 */
	private Double countcharge;
	/**
	 * 保额
	 */
	private Object amount;
	/**
	 * 不计免赔
	 */
	private StrongInsuranceBean strongInsuranceBean;
	
	public StrongInsuranceBean getStrongInsuranceBean() {
		return strongInsuranceBean;
	}
	public void setStrongInsuranceBean(StrongInsuranceBean strongInsuranceBean) {
		this.strongInsuranceBean = strongInsuranceBean;
	}
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
	public Object getAmount() {
		return amount;
	}
	public void setAmount(Object amount) {
		this.amount = amount;
	}
	
}
