package com.zzb.conf.controller.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BillTypeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<InterfaceVo> agreementInterfaceList;
	
	private String channelid;
	private String deptid;
	private String agreementid;
	/**
	 * 渠道名称
	 */
	private String channelname;
	/**
	 * 关联机构
	 */
	private String deptname;
	/**
	 * 结算周期
	 */
	private String payperiod;
	
	/**
	 * 结算方式
	 */
	private String paytype;
	
	/**
	 * 结算账户
	 */
	private String payaccount;
	
	/**
	 * 接口接入方式
	 */
	private String interfacetype;


	public List<InterfaceVo> getAgreementInterfaceList() {
		return agreementInterfaceList;
	}

	public void setAgreementInterfaceList(List<InterfaceVo> agreementInterfaceList) {
		this.agreementInterfaceList = agreementInterfaceList;
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

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}
	
}
