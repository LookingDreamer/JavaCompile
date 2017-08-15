package com.zzb.model;

public class SelectInsuredConfigBean {

	//private String id;
	/**
	 * 险别名称
	 */
	private String riskname;
	/**
	 * 险别编码
	 */
	private String inskindcode;
	/**
	 * 类型 0商业险 1不计免赔 2交强险 3 车船税
	 */
	//private String inskindtype;
	/**
	 * 保额
	 */
	private Double amount;
	/**
	 * 保费
	 */
	private Double discountCharge;
	public String getRiskname() {
		return riskname;
	}
	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}
	public String getInskindcode() {
		return inskindcode;
	}
	public void setInskindcode(String inskindcode) {
		this.inskindcode = inskindcode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getDiscountCharge() {
		return discountCharge;
	}
	public void setDiscountCharge(Double discountCharge) {
		this.discountCharge = discountCharge;
	}
	
}
