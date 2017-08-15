package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgentBankcard extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 序号 
	 */
	 private String rownum;
	/**
	 * 卡号前缀
	 */
	private String cardprefix;

	/**
	 * 卡名
	 */
	private String cardname;

	/**
	 * 发卡银行名称
	 */
	private String banktoname;

	/**
	 * 卡种
	 */
	private String cardtype;

	/**
	 * 卡号长度
	 */
	private String cardnumlength;

	/**
	 * 发卡行机构编码
	 */
	private String cardbankdeptcode;

	/** 卡号所属代理人*/
	private String cardagentid;

	/** '卡号' */
	private String cardno;

	/** 支行所属省 */
	private String province;
	/** 支行所属市 */
	private String city;
	/** 支行具体名称 */
	private String branchbank;
	/** 是否默认卡 */
	private String isdefault = "0";

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranchbank() {
		return branchbank;
	}

	public void setBranchbank(String branchbank) {
		this.branchbank = branchbank;
	}

	public String getCardagentid() {
		return cardagentid;
	}

	public void setCardagentid(String cardagentid) {
		this.cardagentid = cardagentid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardprefix() {
		return cardprefix;
	}

	public void setCardprefix(String cardprefix) {
		this.cardprefix = cardprefix;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getBanktoname() {
		return banktoname;
	}

	public void setBanktoname(String banktoname) {
		this.banktoname = banktoname;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardnumlength() {
		return cardnumlength;
	}

	public void setCardnumlength(String cardnumlength) {
		this.cardnumlength = cardnumlength;
	}

	public String getCardbankdeptcode() {
		return cardbankdeptcode;
	}

	public void setCardbankdeptcode(String cardbankdeptcode) {
		this.cardbankdeptcode = cardbankdeptcode;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

}