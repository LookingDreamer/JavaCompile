package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgreementprovider extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
 
	/**
	 * 协议ID
	 */
	private String agreementid;

	/**
	 * 供应商ID
	 */
	private String providerid;

	/**
	 * agreeid
	 */
	private String agreeid;

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

	public String getAgreeid() {
		return agreeid;
	}

	public void setAgreeid(String agreeid) {
		this.agreeid = agreeid;
	}

	
}