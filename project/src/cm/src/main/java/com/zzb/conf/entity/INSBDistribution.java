package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBDistribution extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String agreementid;

	/**
	 * 
	 */
	private String deptid;

	/**
	 * 配送方式
            字典表 codetype  =distritype
	 */
	private String distritype;

	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 快递公司
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

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDistritype() {
		return distritype;
	}

	public void setDistritype(String distritype) {
		this.distritype = distritype;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
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