package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBProviderandedi extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * edi表id
	 */
	private String ediid;

	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 备用（状态）
	 */
	private String state;


	public String getEdiid() {
		return ediid;
	}

	public void setEdiid(String ediid) {
		this.ediid = ediid;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}