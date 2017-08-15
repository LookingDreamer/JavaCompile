package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBDistributiontype extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 协议ID
	 */
	private String agreementid;

	/**
	 * 配送方式
            字典表 codetype  =distritype
	 */
	private String distritype;

	/**
	 * 
	 */
	private String distrcompany;

	/**
	 * 费用收取方式
            字典表 codetype  distpaytype
	 */
	private String distpaytype;

	/**
	 * 预收费用
	 */
	private Double chargefee;

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getDistritype() {
		return distritype;
	}

	public void setDistritype(String distritype) {
		this.distritype = distritype;
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

	public Double getChargefee() {
		return chargefee;
	}

	public void setChargefee(Double chargefee) {
		this.chargefee = chargefee;
	}

}