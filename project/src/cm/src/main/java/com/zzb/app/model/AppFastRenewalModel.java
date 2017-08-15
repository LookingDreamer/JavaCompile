package com.zzb.app.model;

public class AppFastRenewalModel {

	/**
	 * 地区ID
	 */
	private String zone;
	/**
	 * 销售价格渠道
	 */
	private String channe;
	/**
	 * 代理人所属网点编码
	 */
	private String comCode;
	/**
	 * 代理人工号
	 */
	private String jobId;
	/**
	 * 投保方式（net 网销，phone 电销，human 地面）
	 */
	private String insureWay;
	/**
	 * 渠道是否可用(可用的：true,不可用：false)
	 */
	private String isAvailable;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getChanne() {
		return channe;
	}
	public void setChanne(String channe) {
		this.channe = channe;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getInsureWay() {
		return insureWay;
	}
	public void setInsureWay(String insureWay) {
		this.insureWay = insureWay;
	}
	public String getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}
	
}
