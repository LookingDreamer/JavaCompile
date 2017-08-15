package com.lzgapi.order.model;

public class LzgPolicyModel {
	/**
	 * 保单号
	 */
	private String contNo;
	/**
	 * 保险公司编号
	 */
	private String supplierCode;
	
	/**
	 * 险种编码
	 */
	private String riskCode;
	/**
	 * 出单日期
	 */
	private String signDate;
	/**
	 * 起止日期
	 */
	private String cvaliDate;
	/**
	 * 终止日期
	 */
	private String cinvaliDate;
	/**
	 * 保费
	 */
	private String contPrem;
	/**
	 * 保额
	 */
	private String amnt;
	/**
	 * 被保人姓名
	 */
	private String insuredName;
	/**
	 * 被保人证件号
	 */
	private String insuredIdno;
	public String getContNo() {
		return contNo;
	}
	public void setContNo(String contNo) {
		this.contNo = contNo;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getCvaliDate() {
		return cvaliDate;
	}
	public void setCvaliDate(String cvaliDate) {
		this.cvaliDate = cvaliDate;
	}
	public String getCinvaliDate() {
		return cinvaliDate;
	}
	public void setCinvaliDate(String cinvaliDate) {
		this.cinvaliDate = cinvaliDate;
	}
	public String getContPrem() {
		return contPrem;
	}
	public void setContPrem(String contPrem) {
		this.contPrem = contPrem;
	}
	public String getAmnt() {
		return amnt;
	}
	public void setAmnt(String amnt) {
		this.amnt = amnt;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getInsuredIdno() {
		return insuredIdno;
	}
	public void setInsuredIdno(String insuredIdno) {
		this.insuredIdno = insuredIdno;
	}
	
	
}
