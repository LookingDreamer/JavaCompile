package com.zzb.chn.bean.third;

public class RiskKindBean {
	private Integer riskCode;
	private Double amount;
	private boolean notDeductible;
	private Double premium;
	private Double ncfPreminm;
	
	public Integer getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(Integer riskCode) {
		this.riskCode = riskCode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public boolean isNotDeductible() {
		return notDeductible;
	}
	public void setNotDeductible(boolean notDeductible) {
		this.notDeductible = notDeductible;
	}
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	public Double getNcfPreminm() {
		return ncfPreminm;
	}
	public void setNcfPreminm(Double ncfPreminm) {
		this.ncfPreminm = ncfPreminm;
	}
	
}
