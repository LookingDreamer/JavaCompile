package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class RiskKindBean {
	private String riskCode;
	private String riskName;
	private String amountDesc;
	private String amount;
	private String originalPremium;
	private String discountPremium;
	private String discountRate;
	private String notDeductible;
	private String ncfRiskCode;
	private String ncfOriginalPremium;
	private String ncfDiscountPremium;
	private String ncfDiscountRate;
	private String premium;
	private String ncfPremium;

	
	
	public String getNcfPremium() {
		return ncfPremium;
	}

	public void setNcfPremium(String ncfPremium) {
		this.ncfPremium = ncfPremium;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getAmountDesc() {
		return amountDesc;
	}

	public void setAmountDesc(String amountDesc) {
		this.amountDesc = amountDesc;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOriginalPremium() {
		return originalPremium;
	}

	public void setOriginalPremium(String originalPremium) {
		this.originalPremium = originalPremium;
	}

	public String getDiscountPremium() {
		return discountPremium;
	}

	public void setDiscountPremium(String discountPremium) {
		this.discountPremium = discountPremium;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getNotDeductible() {
		return notDeductible;
	}

	public void setNotDeductible(String notDeductible) {
		this.notDeductible = notDeductible;
	}

	public String getNcfRiskCode() {
		return ncfRiskCode;
	}

	public void setNcfRiskCode(String ncfRiskCode) {
		this.ncfRiskCode = ncfRiskCode;
	}

	public String getNcfOriginalPremium() {
		return ncfOriginalPremium;
	}

	public void setNcfOriginalPremium(String ncfOriginalPremium) {
		this.ncfOriginalPremium = ncfOriginalPremium;
	}

	public String getNcfDiscountPremium() {
		return ncfDiscountPremium;
	}

	public void setNcfDiscountPremium(String ncfDiscountPremium) {
		this.ncfDiscountPremium = ncfDiscountPremium;
	}

	public String getNcfDiscountRate() {
		return ncfDiscountRate;
	}

	public void setNcfDiscountRate(String ncfDiscountRate) {
		this.ncfDiscountRate = ncfDiscountRate;
	}

}
 