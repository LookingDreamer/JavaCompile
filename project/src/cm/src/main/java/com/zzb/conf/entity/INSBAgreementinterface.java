package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgreementinterface extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 协议ID
	 */
	private String agreementid;

	/**
	 * 接口ID
	 */
	private String interfaceid;

	/**
	 * 是否免费 0-免费  1-收费
	 */
	private String isfree;

	/**
	 * 每月免费次数
	 */
	private Integer monthfree;

	/**
	 * 每次费用
	 */
	private Double perfee;

	/**
	 * 开启状态 0-禁用 1-开启
	 */
	private String status;

	/**
	 * 免费次数用完后能否继续使用 0-收费模式 1-禁止使用
	 */
	private String extendspattern;

	private String channelinnercode; //渠道内部编码

	/**
	 * 扩展参数1 0-关闭  1-开启
	 */
	private String pv1;
	/**
	 * 扩展参数2 0-关闭  1-开启
	 */
	private String pv2;
	/**
	 * 扩展参数3 0-关闭  1-开启
	 */
	private String pv3;
	/**
	 * 扩展参数4 0-关闭  1-开启
	 */
	private String pv4;

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getInterfaceid() {
		return interfaceid;
	}

	public void setInterfaceid(String interfaceid) {
		this.interfaceid = interfaceid;
	}

	public String getIsfree() {
		return isfree;
	}

	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}

	public Integer getMonthfree() {
		return monthfree;
	}

	public void setMonthfree(Integer monthfree) {
		this.monthfree = monthfree;
	}

	public Double getPerfee() {
		return perfee;
	}

	public void setPerfee(Double perfee) {
		this.perfee = perfee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChannelinnercode() {
		return channelinnercode;
	}

	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
	}

	public String getExtendspattern() {
		return extendspattern;
	}

	public void setExtendspattern(String extendspattern) {
		this.extendspattern = extendspattern;
	}

	public String getPv1() {
		return pv1;
	}

	public void setPv1(String pv1) {
		this.pv1 = pv1;
	}

	public String getPv2() {
		return pv2;
	}

	public void setPv2(String pv2) {
		this.pv2 = pv2;
	}

	public String getPv3() {
		return pv3;
	}

	public void setPv3(String pv3) {
		this.pv3 = pv3;
	}

	public String getPv4() {
		return pv4;
	}

	public void setPv4(String pv4) {
		this.pv4 = pv4;
	}
}