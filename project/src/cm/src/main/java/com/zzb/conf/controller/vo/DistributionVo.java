package com.zzb.conf.controller.vo;

import java.io.Serializable;
import java.util.Date;

public class DistributionVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String selfdistritype; 
	private String selfnoti;
	private String distdistritype;
	private String distrcompany;
	private String distpaytype;
	private String chargefee;
	private String distnoti;
	private String agreementid;
	private String providerid;
	private String deptid;
	
	public String getAgreementid() {
		return agreementid;
	}
	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}
	public String getProviderid() {
		return providerid;
	}
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getSelfnoti() {
		return selfnoti;
	}
	public void setSelfnoti(String selfnoti) {
		this.selfnoti = selfnoti;
	}
	public String getDistrcompany() {
		return distrcompany;
	}
	public void setDistrcompany(String distrcompany) {
		this.distrcompany = distrcompany;
	}
	public String getDistpaytype() {
		return distpaytype;
	}
	public void setDistpaytype(String distpaytype) {
		this.distpaytype = distpaytype;
	}
	public String getDistnoti() {
		return distnoti;
	}
	public void setDistnoti(String distnoti) {
		this.distnoti = distnoti;
	}
	public String getSelfdistritype() {
		return selfdistritype;
	}
	public void setSelfdistritype(String selfdistritype) {
		this.selfdistritype = selfdistritype;
	}
	public String getDistdistritype() {
		return distdistritype;
	}
	public void setDistdistritype(String distdistritype) {
		this.distdistritype = distdistritype;
	}
	public String getChargefee() {
		return chargefee;
	}
	public void setChargefee(String chargefee) {
		this.chargefee = chargefee;
	}
	
}
