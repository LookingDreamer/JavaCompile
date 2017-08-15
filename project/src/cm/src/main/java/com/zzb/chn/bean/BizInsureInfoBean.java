package com.zzb.chn.bean;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BizInsureInfoBean extends BaseInsureInfoBean {
	private  String nfcPremium;//不计免赔总保费
	private List<RiskKindBean> riskKinds;
	private String premium; //"商业险保费",
	private String startDate;//商业险起保日期
	private String endDate;//商业险终保日期

	public List<RiskKindBean> getRiskKinds() {
		return riskKinds;
	}

	public void setRiskKinds(List<RiskKindBean> riskKinds) {
		this.riskKinds = riskKinds;
	}

	@Override
	public String getStartDate() {
		return startDate;
	}

	@Override
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Override
	public String getEndDate() {
		return endDate;
	}

	@Override
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getPremium() {
		return premium;
	}

	@Override
	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getNfcPremium() {
		return nfcPremium;
	}

	public void setNfcPremium(String nfcPremium) {
		this.nfcPremium = nfcPremium;
	}
}
