package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("insureDate")
public class InsureDate {
	/**
	 * 交强险开始日期
	 */
	@XStreamAlias("trafficInsEffectDate")
	private String trafficInsEffectDate;
	/**
	 * 交强险结束日期
	 */
	@XStreamAlias("trafficInsInvalidDate")
	private String trafficInsInvalidDate;
	/**
	 * 商业险开始日期
	 */
	@XStreamAlias("businessInsEffectDate")
	private String businessInsEffectDate;
	/**
	 * 商业险结束日期
	 */
	@XStreamAlias("businessInsInvalidDate")
	private String businessInsInvalidDate;
	
	public String getTrafficInsEffectDate() {
		return trafficInsEffectDate;
	}
	public void setTrafficInsEffectDate(String trafficInsEffectDate) {
		this.trafficInsEffectDate = trafficInsEffectDate;
	}
	public String getTrafficInsInvalidDate() {
		return trafficInsInvalidDate;
	}
	public void setTrafficInsInvalidDate(String trafficInsInvalidDate) {
		this.trafficInsInvalidDate = trafficInsInvalidDate;
	}
	public String getBusinessInsEffectDate() {
		return businessInsEffectDate;
	}
	public void setBusinessInsEffectDate(String businessInsEffectDate) {
		this.businessInsEffectDate = businessInsEffectDate;
	}
	public String getBusinessInsInvalidDate() {
		return businessInsInvalidDate;
	}
	public void setBusinessInsInvalidDate(String businessInsInvalidDate) {
		this.businessInsInvalidDate = businessInsInvalidDate;
	}
	
}
