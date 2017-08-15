package com.zzb.mobile.model;

import java.io.Serializable;
import java.util.List;

public class LastYearClaimBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int claimtimes;//理赔次数
	double claimrate;//理赔率
	String jqclaimtimes;//交强理赔次数
	double jqclaimrate;//交强理赔率
	String firstinsuretype;//投保类型 非首次投保:0,新车首次投保:1,旧车首次投保:2
	double lastclaimsum;//理赔金额
	double jqlastclaimsum;//交强理赔金额
	String trafficoffence;//交通违规次数
	double trafficoffencediscount;//交通违规系数
	String bwcommercialclaimtimes; // 商业险理赔次数
	String noclaimdiscountcoefficient;// 无赔款优待系数-
	Double compulsoryclaimrate;// 交强险理赔系数-
	String bwcompulsoryclaimtimes;//交强险理赔次数
	String noclaimdiscountcoefficientreasons;// 无赔款折扣浮动原因-
	String compulsoryclaimratereasons;// 交强险事故浮动原因
	List<LastYearBizClaimsBean> syclaims;//商业险事故
	List<LastYearBizClaimsBean> jqclaims;//交强险事故
	
	public List<LastYearBizClaimsBean> getSyclaims() {
		return syclaims;
	}
	public void setSyclaims(List<LastYearBizClaimsBean> syclaims) {
		this.syclaims = syclaims;
	}
	public List<LastYearBizClaimsBean> getJqclaims() {
		return jqclaims;
	}
	public void setJqclaims(List<LastYearBizClaimsBean> jqclaims) {
		this.jqclaims = jqclaims;
	}
	public String getNoclaimdiscountcoefficient() {
		return noclaimdiscountcoefficient;
	}
	public void setNoclaimdiscountcoefficient(String noclaimdiscountcoefficient) {
		this.noclaimdiscountcoefficient = noclaimdiscountcoefficient;
	}
	public Double getCompulsoryclaimrate() {
		return compulsoryclaimrate;
	}
	public void setCompulsoryclaimrate(Double compulsoryclaimrate) {
		this.compulsoryclaimrate = compulsoryclaimrate;
	}
	public String getBwcompulsoryclaimtimes() {
		return bwcompulsoryclaimtimes;
	}
	public void setBwcompulsoryclaimtimes(String bwcompulsoryclaimtimes) {
		this.bwcompulsoryclaimtimes = bwcompulsoryclaimtimes;
	}
	public String getNoclaimdiscountcoefficientreasons() {
		return noclaimdiscountcoefficientreasons;
	}
	public void setNoclaimdiscountcoefficientreasons(
			String noclaimdiscountcoefficientreasons) {
		this.noclaimdiscountcoefficientreasons = noclaimdiscountcoefficientreasons;
	}
	public String getCompulsoryclaimratereasons() {
		return compulsoryclaimratereasons;
	}
	public void setCompulsoryclaimratereasons(String compulsoryclaimratereasons) {
		this.compulsoryclaimratereasons = compulsoryclaimratereasons;
	}
	public String getBwcommercialclaimtimes() {
		return bwcommercialclaimtimes;
	}
	public void setBwcommercialclaimtimes(String bwcommercialclaimtimes) {
		this.bwcommercialclaimtimes = bwcommercialclaimtimes;
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
	public String getJqclaimtimes() {
		return jqclaimtimes;
	}
	public void setJqclaimtimes(String jqclaimtimes) {
		this.jqclaimtimes = jqclaimtimes;
	}
	public double getJqclaimrate() {
		return jqclaimrate;
	}
	public void setJqclaimrate(double jqclaimrate) {
		this.jqclaimrate = jqclaimrate;
	}
	public String getFirstinsuretype() {
		return firstinsuretype;
	}
	public void setFirstinsuretype(String firstinsuretype) {
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
	public String getTrafficoffence() {
		return trafficoffence;
	}
	public void setTrafficoffence(String trafficoffence) {
		this.trafficoffence = trafficoffence;
	}
	public double getTrafficoffencediscount() {
		return trafficoffencediscount;
	}
	public void setTrafficoffencediscount(double trafficoffencediscount) {
		this.trafficoffencediscount = trafficoffencediscount;
	}
}
