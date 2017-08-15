package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBChannelagreement extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 协议名称 
	 */
	private String agreementname;

	/**
	 * 渠道ID
	 */
	private String channelid;

	/**
	 * 机构ID
	 */
	private String deptid;

	/**
	 * 协议编码
	 */
	private String agreementcode;

	/**
	 * 协议状态 1-启用   0-停用
	 */
	private String agreementstatus;

	/**
	 * 结算周期
	 */
	private String payperiod;

	/**
	 * 结算方式  0-银行转账  1-其他方式
	 */
	private String paytype;

	/**
	 * 结算账户
	 */
	private String payaccount;

	/**
	 * 接口接入方式  0-微信公众号  1-App  2-网页
	 */
	private String interfacetype;

	public String getAgreementname() {
		return agreementname;
	}

	public void setAgreementname(String agreementname) {
		this.agreementname = agreementname;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getAgreementcode() {
		return agreementcode;
	}

	public void setAgreementcode(String agreementcode) {
		this.agreementcode = agreementcode;
	}

	public String getAgreementstatus() {
		return agreementstatus;
	}

	public void setAgreementstatus(String agreementstatus) {
		this.agreementstatus = agreementstatus;
	}

	public String getPayperiod() {
		return payperiod;
	}

	public void setPayperiod(String payperiod) {
		this.payperiod = payperiod;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getPayaccount() {
		return payaccount;
	}

	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}

	public String getInterfacetype() {
		return interfacetype;
	}

	public void setInterfacetype(String interfacetype) {
		this.interfacetype = interfacetype;
	}

}