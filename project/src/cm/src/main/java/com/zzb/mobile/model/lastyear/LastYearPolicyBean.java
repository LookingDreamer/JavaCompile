package com.zzb.mobile.model.lastyear;

import java.io.Serializable;

public class LastYearPolicyBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 保险公司id
	 */
	private String supplierid;
	/**
	 * 保险公司名称
	 */
	private String prvname;
	/**
	 * 
	 */
	private String status;
	/**
	 * 车主姓名
	 */
	private String ownerrealname;
	/**
	 * 证件号
	 */
	private String owneridno;
	/**
	 * 联系电话
	 */
	private String ownermobile;
	/**
	 * email
	 */
	private String ownermail;
	/**
	 * 总保费
	 */
	private Double sumprem;
	/**
	 * 商业险保费
	 */
	private Double syprem;
	/**
	 * 交强保险费
	 */
	private Double jqprem;
	/**
	 * 折扣
	 */
	private Double discount;
	/**
	 * 车船税
	 */
	private Double tax;//
	//被保人姓名，证件号，联系电话，email(无)
	/**
	 * 被保人姓名
	 */
	private String insuredname;
	/**
	 * 被保人证件类型
	 */
	private String inseredcertificate;
	/**
	 * 被保人证件号
	 */
	private String insuredno;
	/**
	 * 联系电话
	 */
	private String insuredmobile;
	//投保人姓名，证件号，联系电话，email(无)
	/**
	 * 投保人姓名
	 */
	private String propname;
	/**
	 * 投保人证件类型
	 */
	private String propcertificate;
	/**
	 * 投保人证件号
	 */
	private String propno;
	/**
	 * 投保人手机号
	 */
	private String propmobile;
	/**
	 * 
	 */
	private LastYearCarinfoBean lastYearCarinfoBean;
	private LastYearPolicyConfBean lastYearPolicyConfBean;
	/**
	 * 保单号
	 */
	private String policyno;
	/**
	 * 交强保单号
	 */
	private String jqpolicyno;
	/**
	 * 理赔次数
	 */
	private int claimtimes;
	/**
	 * 理赔率
	 */
	private double claimrate;
	/**
	 * 交强理赔次数
	 */
	private int jqclaimtimes;
	/**
	 * 交强理赔率
	 */
	private double jqclaimrate;
	/**
	 * 投保类型 非首次投保:0,新车首次投保:1,旧车首次投保:2
	 */
	private int firstinsuretype;
	/**
	 * 理赔金额
	 */
	private double lastclaimsum;
	/**
	 * 交强理赔金额
	 */
	private double jqlastclaimsum;
	/**
	 * 交通违规次数
	 */
	private int trafficoffence;
	/**
	 * 交通违规系数
	 */
	private double trafficoffencediscount;
	
	public String getInsuredmobile() {
		return insuredmobile;
	}

	public void setInsuredmobile(String insuredmobile) {
		this.insuredmobile = insuredmobile;
	}

	public String getPropmobile() {
		return propmobile;
	}

	public void setPropmobile(String propmobile) {
		this.propmobile = propmobile;
	}

	public String getPolicyno() {
		return policyno;
	}

	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}

	public String getJqpolicyno() {
		return jqpolicyno;
	}

	public void setJqpolicyno(String jqpolicyno) {
		this.jqpolicyno = jqpolicyno;
	}

	public int getClaimtimes() {
		return claimtimes;
	}

	public void setClaimtimes(int claimtimes) {
		this.claimtimes = claimtimes;
	}

	public double getClaimrate() {
		return claimrate;
	}

	public void setClaimrate(double claimrate) {
		this.claimrate = claimrate;
	}

	public int getJqclaimtimes() {
		return jqclaimtimes;
	}

	public void setJqclaimtimes(int jqclaimtimes) {
		this.jqclaimtimes = jqclaimtimes;
	}

	public double getJqclaimrate() {
		return jqclaimrate;
	}

	public void setJqclaimrate(double jqclaimrate) {
		this.jqclaimrate = jqclaimrate;
	}

	public int getFirstinsuretype() {
		return firstinsuretype;
	}

	public void setFirstinsuretype(int firstinsuretype) {
		this.firstinsuretype = firstinsuretype;
	}

	public double getLastclaimsum() {
		return lastclaimsum;
	}

	public void setLastclaimsum(double lastclaimsum) {
		this.lastclaimsum = lastclaimsum;
	}

	public double getJqlastclaimsum() {
		return jqlastclaimsum;
	}

	public void setJqlastclaimsum(double jqlastclaimsum) {
		this.jqlastclaimsum = jqlastclaimsum;
	}

	public int getTrafficoffence() {
		return trafficoffence;
	}

	public void setTrafficoffence(int trafficoffence) {
		this.trafficoffence = trafficoffence;
	}

	public double getTrafficoffencediscount() {
		return trafficoffencediscount;
	}

	public void setTrafficoffencediscount(double trafficoffencediscount) {
		this.trafficoffencediscount = trafficoffencediscount;
	}

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

	public Double getJqprem() {
		return jqprem;
	}

	public void setJqprem(Double jqprem) {
		this.jqprem = jqprem;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public String getInsuredname() {
		return insuredname;
	}

	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}

	public String getInseredcertificate() {
		return inseredcertificate;
	}

	public void setInseredcertificate(String inseredcertificate) {
		this.inseredcertificate = inseredcertificate;
	}

	public String getInsuredno() {
		return insuredno;
	}

	public void setInsuredno(String insuredno) {
		this.insuredno = insuredno;
	}

	public String getPropname() {
		return propname;
	}

	public void setPropname(String propname) {
		this.propname = propname;
	}

	public String getPropcertificate() {
		return propcertificate;
	}

	public void setPropcertificate(String propcertificate) {
		this.propcertificate = propcertificate;
	}

	public String getPropno() {
		return propno;
	}

	public void setPropno(String propno) {
		this.propno = propno;
	}

	public String getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}

	public LastYearCarinfoBean getLastYearCarinfoBean() {
		return lastYearCarinfoBean;
	}

	public void setLastYearCarinfoBean(LastYearCarinfoBean lastYearCarinfoBean) {
		this.lastYearCarinfoBean = lastYearCarinfoBean;
	}

	public LastYearPolicyConfBean getLastYearPolicyConfBean() {
		return lastYearPolicyConfBean;
	}

	public void setLastYearPolicyConfBean(
			LastYearPolicyConfBean lastYearPolicyConfBean) {
		this.lastYearPolicyConfBean = lastYearPolicyConfBean;
	}

	public String getPrvname() {
		return prvname;
	}

	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwnerrealname() {
		return ownerrealname;
	}

	public void setOwnerrealname(String ownerrealname) {
		this.ownerrealname = ownerrealname;
	}

	public String getOwneridno() {
		return owneridno;
	}

	public void setOwneridno(String owneridno) {
		this.owneridno = owneridno;
	}

	public String getOwnermobile() {
		return ownermobile;
	}

	public void setOwnermobile(String ownermobile) {
		this.ownermobile = ownermobile;
	}

	public String getOwnermail() {
		return ownermail;
	}

	public void setOwnermail(String ownermail) {
		this.ownermail = ownermail;
	}

}
