package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRisk extends BaseEntity implements Identifiable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 行号
	 */
	private String rownum;

	/**
	 * 
	 */
	private String riskcode;

	/**
	 * 
	 */
	private String riskname;

	/**
	 * 
	 */
	private String riskshortname;

	/**
	 * 
	 */
	private String risktype;

	/**
	 * 
	 */
	private String provideid;
	
	/**
	 * 
	 */
	private String providename;

	/**
	 * 
	 */
	private String status;

	/**
	 * 1-快速续保 2-保险配置必须填写
	 */
	private String renewaltype;

	public String getRiskcode() {
		return riskcode;
	}

	public void setRiskcode(String riskcode) {
		this.riskcode = riskcode;
	}

	public String getRiskname() {
		return riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public String getRiskshortname() {
		return riskshortname;
	}

	public void setRiskshortname(String riskshortname) {
		this.riskshortname = riskshortname;
	}

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

	public String getProvideid() {
		return provideid;
	}

	public void setProvideid(String provideid) {
		this.provideid = provideid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRenewaltype() {
		return renewaltype;
	}

	public void setRenewaltype(String renewaltype) {
		this.renewaltype = renewaltype;
	}

	public String getProvidename() {
		return providename;
	}

	public void setProvidename(String providename) {
		this.providename = providename;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	@Override
	public String toString() {
		return "INSBRisk [riskcode=" + riskcode + ", riskname=" + riskname
				+ ", riskshortname=" + riskshortname + ", risktype=" + risktype
				+ ", provideid=" + provideid + ", providename=" + providename
				+ ", status=" + status + ", renewaltype=" + renewaltype + "]";
	}
	
	

}
