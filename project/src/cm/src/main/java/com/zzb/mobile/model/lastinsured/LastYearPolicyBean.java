package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;

public class LastYearPolicyBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Double sumprem;//总保费
	Double syprem;//商业险保费
	Double jqprem;//交强保险费
	Double discount;//折扣
	Double tax;//车船税
	String policyno;//保单号
	String jqpolicyno;//交强保单号
	LastYearPersonBean insureder;//被保人
	LastYearPersonBean proper;//投保人
	String startdate;//商业险投保期间
	String enddate;
	String jqstartdate;//交强险保险期间
	String jqenddate;
	
	public Double getSumprem() {
		return sumprem;
	}
	public void setSumprem(Double sumprem) {
		this.sumprem = sumprem;
	}
	public Double getSyprem() {
		return syprem;
	}
	public void setSyprem(Double syprem) {
		this.syprem = syprem;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getJqprem() {
		return jqprem;
	}
	public void setJqprem(Double jqprem) {
		this.jqprem = jqprem;
	}
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno==null?null:policyno.trim();
	}
	public String getJqpolicyno() {
		return jqpolicyno;
	}
	public void setJqpolicyno(String jqpolicyno) {
		this.jqpolicyno = jqpolicyno==null?null:jqpolicyno.trim();
	}
	public LastYearPersonBean getInsureder() {
		return insureder;
	}
	public void setInsureder(LastYearPersonBean insureder) {
		this.insureder = insureder;
	}
	public LastYearPersonBean getProper() {
		return proper;
	}
	public void setProper(LastYearPersonBean proper) {
		this.proper = proper;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate==null?null:startdate.trim();
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate==null?null:enddate.trim();
	}
	public String getJqstartdate() {
		return jqstartdate;
	}
	public void setJqstartdate(String jqstartdate) {
		this.jqstartdate = jqstartdate==null?null:jqstartdate.trim();
	}
	public String getJqenddate() {
		return jqenddate;
	}
	public void setJqenddate(String jqenddate) {
		this.jqenddate =  jqenddate==null?null:jqenddate.trim();
	}
}
