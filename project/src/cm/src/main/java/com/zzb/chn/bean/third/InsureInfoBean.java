package com.zzb.chn.bean.third;

import java.util.List;

public class InsureInfoBean {
	private String startDate;
	private Double premium;
	private Double discountRate;
	private Integer businessType;
	private Integer prevUsedCount;
	private Integer prevDamagedCI;
	private Integer insuranceType;
	private String insuranceSlipNo;
	private List<RiskKindBean> riskKinds;
	
	public String getInsuranceSlipNo() {
		return insuranceSlipNo;
	}
	public void setInsuranceSlipNo(String insuranceSlipNo) {
		this.insuranceSlipNo = insuranceSlipNo;
	}
	public List<RiskKindBean> getRiskKinds() {
		return riskKinds;
	}
	public void setRiskKinds(List<RiskKindBean> riskKinds) {
		this.riskKinds = riskKinds;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	public Double getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}
	public Integer getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	public Integer getPrevUsedCount() {
		return prevUsedCount;
	}
	public void setPrevUsedCount(Integer prevUsedCount) {
		this.prevUsedCount = prevUsedCount;
	}
	public Integer getPrevDamagedCI() {
		return prevDamagedCI;
	}
	public void setPrevDamagedCI(Integer prevDamagedCI) {
		this.prevDamagedCI = prevDamagedCI;
	}
	public Integer getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(Integer insuranceType) {
		this.insuranceType = insuranceType;
	}
		
}
