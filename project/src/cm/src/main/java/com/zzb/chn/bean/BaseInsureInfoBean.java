package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseInsureInfoBean {
	private String startDate;
	private String endDate;
	private String amount;
	private String premium;
	private String discountPremium;
	private String discountRate;
	private String lateFee;
	private String isPaymentTax;
	private String stampTaxFee;
	private String taxFee;
	private String policyNo;


	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getIsPaymentTax() {
		return isPaymentTax;
	}

	public void setIsPaymentTax(String isPaymentTax) {
		this.isPaymentTax = isPaymentTax;
	}

	public String getStampTaxFee() {
		return stampTaxFee;
	}

	public void setStampTaxFee(String stampTaxFee) {
		this.stampTaxFee = stampTaxFee;
	}

	public String getTaxFee() {
		return taxFee;
	}

	public void setTaxFee(String taxFee) {
		this.taxFee = taxFee;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
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

	public String getLateFee() {
		return lateFee;
	}

	public void setLateFee(String lateFee) {
		this.lateFee = lateFee;
	}

}
