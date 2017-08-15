package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class InsureInfoBean {
	private BaseInsureInfoBean efcInsureInfo;
	private BaseInsureInfoBean taxInsureInfo;
	private BizInsureInfoBean bizInsureInfo;
	private String totalPremium;

	public BaseInsureInfoBean getEfcInsureInfo() {
		return efcInsureInfo;
	}

	public void setEfcInsureInfo(BaseInsureInfoBean efcInsureInfo) {
		this.efcInsureInfo = efcInsureInfo;
	}

	public BaseInsureInfoBean getTaxInsureInfo() {
		return taxInsureInfo;
	}

	public void setTaxInsureInfo(BaseInsureInfoBean taxInsureInfo) {
		this.taxInsureInfo = taxInsureInfo;
	}

	public BizInsureInfoBean getBizInsureInfo() {
		return bizInsureInfo;
	}

	public void setBizInsureInfo(BizInsureInfoBean bizInsureInfo) {
		this.bizInsureInfo = bizInsureInfo;
	}

	public String getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(String totalPremium) {
		this.totalPremium = totalPremium;
	}
}
