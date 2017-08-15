package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBItemprovidestatus extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 权限包ID
	 */
	private String setid;

	/**
	 * 供应商id
	 */
	private String provideid;

	/**
	 * 限定状态
	 */
	private Integer limitstatus;

	public String getSetid() {
		return setid;
	}

	public void setSetid(String setid) {
		this.setid = setid;
	}

	public String getProvideid() {
		return provideid;
	}

	public void setProvideid(String provideid) {
		this.provideid = provideid;
	}

	public Integer getLimitstatus() {
		return limitstatus;
	}

	public void setLimitstatus(Integer limitstatus) {
		this.limitstatus = limitstatus;
	}

}