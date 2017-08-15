package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBOperatingsystem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String paychannelid;

	/**
	 * 字典表code
	 */
	private String insbcodevalue;

	/**
	 * 操作系统类型
	 */
	private String systemtype;

	public String getPaychannelid() {
		return paychannelid;
	}

	public void setPaychannelid(String paychannelid) {
		this.paychannelid = paychannelid;
	}

	public String getInsbcodevalue() {
		return insbcodevalue;
	}

	public void setInsbcodevalue(String insbcodevalue) {
		this.insbcodevalue = insbcodevalue;
	}

	public String getSystemtype() {
		return systemtype;
	}

	public void setSystemtype(String systemtype) {
		this.systemtype = systemtype;
	}

}