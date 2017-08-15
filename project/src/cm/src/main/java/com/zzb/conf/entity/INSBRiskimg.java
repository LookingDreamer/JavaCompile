package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRiskimg extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String riskid;

	/**
	 * 
	 */
	private String riskimgname;

	/**
	 * 
	 */
	private String riskimgtype;

	/**
	 * 
	 */
	private String seriesno;

	/**
	 * 
	 */
	private String isusing;

	public String getRiskid() {
		return riskid;
	}

	public void setRiskid(String riskid) {
		this.riskid = riskid;
	}

	public String getRiskimgname() {
		return riskimgname;
	}

	public void setRiskimgname(String riskimgname) {
		this.riskimgname = riskimgname;
	}

	public String getRiskimgtype() {
		return riskimgtype;
	}

	public void setRiskimgtype(String riskimgtype) {
		this.riskimgtype = riskimgtype;
	}

	public String getSeriesno() {
		return seriesno;
	}

	public void setSeriesno(String seriesno) {
		this.seriesno = seriesno;
	}

	public String getIsusing() {
		return isusing;
	}

	public void setIsusing(String isusing) {
		this.isusing = isusing;
	}

}