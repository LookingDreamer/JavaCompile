package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgreementpaymethod extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
 
	/**
	 * 机构ID
	 */
	private String deptid;

	/**
	 * 协议ID
	 */
	private String agreementid;

	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 支付通道ID
	 */
	private String paychannelid;

	/**
	 * 
	 */
	private String channelmanagerid;

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

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

	public String getPaychannelid() {
		return paychannelid;
	}

	public void setPaychannelid(String paychannelid) {
		this.paychannelid = paychannelid;
	}

	public String getChannelmanagerid() {
		return channelmanagerid;
	}

	public void setChannelmanagerid(String channelmanagerid) {
		this.channelmanagerid = channelmanagerid;
	}

}