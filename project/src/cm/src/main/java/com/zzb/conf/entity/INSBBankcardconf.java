package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBBankcardconf extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 字典表code
	 */
	private String insbcodevalue;

	/**
	 * 支付通道表id
	 */
	private String paychannelid;

	/**
	 * 是否为常用银行
	 */
	private String commonlyusedbankflag;

	/**
	 * 是否为储蓄卡
	 */
	private String cashcardflag;

	/**
	 * 是否为信用卡
	 */
	private String creditcardflag;

	/**
	 * 储蓄卡单笔是否限额
	 */
	private String cashlimitfalg;

	/**
	 * 储蓄卡单笔限额
	 */
	private Double cashlimit;

	/**
	 * 储蓄卡日支付是否限额
	 */
	private String cashlimitdayfalg;

	/**
	 * 储蓄卡日支付限额
	 */
	private Double cashdaylimit;

	/**
	 * 信用卡单笔是否限额
	 */
	private String creditlimitfalg;

	/**
	 * 信用卡单笔限额
	 */
	private Double creditlimit;

	/**
	 * 信用卡日支付是否限额
	 */
	private String creditlimitdayfalg;

	/**
	 * 信用卡日支付限额
	 */
	private Double creditdaylimit;

	public String getInsbcodevalue() {
		return insbcodevalue;
	}

	public void setInsbcodevalue(String insbcodevalue) {
		this.insbcodevalue = insbcodevalue;
	}

	public String getPaychannelid() {
		return paychannelid;
	}

	public void setPaychannelid(String paychannelid) {
		this.paychannelid = paychannelid;
	}

	public String getCommonlyusedbankflag() {
		return commonlyusedbankflag;
	}

	public void setCommonlyusedbankflag(String commonlyusedbankflag) {
		this.commonlyusedbankflag = commonlyusedbankflag;
	}

	public String getCashcardflag() {
		return cashcardflag;
	}

	public void setCashcardflag(String cashcardflag) {
		this.cashcardflag = cashcardflag;
	}

	public String getCreditcardflag() {
		return creditcardflag;
	}

	public void setCreditcardflag(String creditcardflag) {
		this.creditcardflag = creditcardflag;
	}

	public String getCashlimitfalg() {
		return cashlimitfalg;
	}

	public void setCashlimitfalg(String cashlimitfalg) {
		this.cashlimitfalg = cashlimitfalg;
	}

	public Double getCashlimit() {
		return cashlimit;
	}

	public void setCashlimit(Double cashlimit) {
		this.cashlimit = cashlimit;
	}

	public String getCashlimitdayfalg() {
		return cashlimitdayfalg;
	}

	public void setCashlimitdayfalg(String cashlimitdayfalg) {
		this.cashlimitdayfalg = cashlimitdayfalg;
	}

	public Double getCashdaylimit() {
		return cashdaylimit;
	}

	public void setCashdaylimit(Double cashdaylimit) {
		this.cashdaylimit = cashdaylimit;
	}

	public String getCreditlimitfalg() {
		return creditlimitfalg;
	}

	public void setCreditlimitfalg(String creditlimitfalg) {
		this.creditlimitfalg = creditlimitfalg;
	}

	public Double getCreditlimit() {
		return creditlimit;
	}

	public void setCreditlimit(Double creditlimit) {
		this.creditlimit = creditlimit;
	}

	public String getCreditlimitdayfalg() {
		return creditlimitdayfalg;
	}

	public void setCreditlimitdayfalg(String creditlimitdayfalg) {
		this.creditlimitdayfalg = creditlimitdayfalg;
	}

	public Double getCreditdaylimit() {
		return creditdaylimit;
	}

	public void setCreditdaylimit(Double creditdaylimit) {
		this.creditdaylimit = creditdaylimit;
	}

	@Override
	public String toString() {
		return "INSBBankcardconf [insbcodevalue=" + insbcodevalue
				+ ", paychannelid=" + paychannelid + ", commonlyusedbankflag="
				+ commonlyusedbankflag + ", cashcardflag=" + cashcardflag
				+ ", creditcardflag=" + creditcardflag + ", cashlimitfalg="
				+ cashlimitfalg + ", cashlimit=" + cashlimit
				+ ", cashlimitdayfalg=" + cashlimitdayfalg + ", cashdaylimit="
				+ cashdaylimit + ", creditlimitfalg=" + creditlimitfalg
				+ ", creditlimit=" + creditlimit + ", creditlimitdayfalg="
				+ creditlimitdayfalg + ", creditdaylimit=" + creditdaylimit
				+ "]";
	}

	
	
}