package com.zzb.cm.Interface.entity.cif.model;


import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


@SuppressWarnings("serial")
public class CarKindPolicy extends BaseEntity implements Identifiable{
	
	String vehicleno; //车牌号
	double sumprem;//总保费
	double discountall;
	double prem;//保费
	double premori;//原保费
	double discount;//折扣
	double avoidprice;//不计免赔总和
	String policyno;//保单号
	double tax;//车船税
	String ifjq;//是否投保交强
	String supplierid;//保险公司
	String agentcode;//代理人编码
	String agentname;//代理人姓名
	Date startdate;//保险起期
	Date enddate;//保险止期
	String propname;//投保人姓名
	String propcertificate;//投保人证件类型
	String propno;//投保人证件编号
	String policysource;//来源
	String propmobile;//投保人联系
	
	/**
	 * 每页显示条数
	 */
	private int limit;
	/**
	 * limit offset, limit;
	 */
	private long offset;

	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public double getSumprem() {
		return sumprem;
	}
	public void setSumprem(double sumprem) {
		this.sumprem = sumprem;
	}
	
	public double getDiscountall() {
		return discountall;
	}
	public void setDiscountall(double discountall) {
		this.discountall = discountall;
	}
	public double getPrem() {
		return prem;
	}
	public void setPrem(double prem) {
		this.prem = prem;
	}
	public double getPremori() {
		return premori;
	}
	public void setPremori(double premori) {
		this.premori = premori;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getAvoidprice() {
		return avoidprice;
	}
	public void setAvoidprice(double avoidprice) {
		this.avoidprice = avoidprice;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public String getIfjq() {
		return ifjq;
	}
	public void setIfjq(String ifjq) {
		this.ifjq = ifjq;
	}
	public String getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}
	public String getAgentcode() {
		return agentcode;
	}
	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
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
	public String getPolicysource() {
		return policysource;
	}
	public void setPolicysource(String policysource) {
		this.policysource = policysource;
	}
	public String getPropmobile() {
		return propmobile;
	}
	public void setPropmobile(String propmobile) {
		this.propmobile = propmobile;
	}
	
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	@Override
	public String toString() {
		return "CarKindPolicy [vehicleno=" + vehicleno + ", sumprem=" + sumprem
				+ " prem=" + prem
				+ ", premori=" + premori + ", discount=" + discount
				+ ", avoidprice=" + avoidprice + ", policyno=" + policyno
				+ ", tax=" + tax + ", ifjq=" + ifjq + ", supplierid="
				+ supplierid + ", agentcode=" + agentcode + ", agentname="
				+ agentname + ", startdate=" + startdate + ", enddate="
				+ enddate + ", propname=" + propname + ", propcertificate="
				+ propcertificate + ", propno=" + propno + ", policysource="
				+ policysource + ", propmobile=" + propmobile + "]";
	}
	
	
	

	
	
	
	
	
}
