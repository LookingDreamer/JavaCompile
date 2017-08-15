package com.zzb.mobile.model;

import java.util.List;

public class AppOtherRequestModel {

	/**
	 * 车主姓名
	 */
	private String carowername;
	/**
	 * 车主姓名
	 */
	private String carlisceno;
	/**
	 * 报价时间
	 */
	private String quotedate;
	/**
	 * 代理人姓名
	 */
	private String agentname;
	/**
	 * 代理人手机号
	 */
	private String agentphone;
	/**
	 * 所有报价公司报价信息
	 */
	private List<QuotaionAllInfo> quotaionAllInfos;

	public String getCarowername() {
		return carowername;
	}

	public void setCarowername(String carowername) {
		this.carowername = carowername;
	}

	public String getCarlisceno() {
		return carlisceno;
	}

	public void setCarlisceno(String carlisceno) {
		this.carlisceno = carlisceno;
	}

	public String getQuotedate() {
		return quotedate;
	}

	public void setQuotedate(String quotedate) {
		this.quotedate = quotedate;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getAgentphone() {
		return agentphone;
	}

	public void setAgentphone(String agentphone) {
		this.agentphone = agentphone;
	}

	public List<QuotaionAllInfo> getQuotaionAllInfos() {
		return quotaionAllInfos;
	}

	public void setQuotaionAllInfos(List<QuotaionAllInfo> quotaionAllInfos) {
		this.quotaionAllInfos = quotaionAllInfos;
	}
}
