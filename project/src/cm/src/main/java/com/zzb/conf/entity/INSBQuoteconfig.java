package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBQuoteconfig extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 协议id
	 */
	private String agreementid;

	/**
	 * 自动-规则报价
	 */
	private String autorulequote;

	/**
	 * 自动-精灵报价
	 */
	private String autojinglingquote;

	/**
	 * 自动-EDI报价
	 */
	private String autoediquote;

	/**
	 * 人工录单
	 */
	private String manualrecord;

	/**
	 * 人工规则录单
	 */
	private String manuarulelrecord;

	/**
	 * 人工调整
	 */
	private String manualadjust;

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getAutorulequote() {
		return autorulequote;
	}

	public void setAutorulequote(String autorulequote) {
		this.autorulequote = autorulequote;
	}

	public String getAutojinglingquote() {
		return autojinglingquote;
	}

	public void setAutojinglingquote(String autojinglingquote) {
		this.autojinglingquote = autojinglingquote;
	}

	public String getAutoediquote() {
		return autoediquote;
	}

	public void setAutoediquote(String autoediquote) {
		this.autoediquote = autoediquote;
	}

	public String getManualrecord() {
		return manualrecord;
	}

	public void setManualrecord(String manualrecord) {
		this.manualrecord = manualrecord;
	}

	public String getManuarulelrecord() {
		return manuarulelrecord;
	}

	public void setManuarulelrecord(String manuarulelrecord) {
		this.manuarulelrecord = manuarulelrecord;
	}

	public String getManualadjust() {
		return manualadjust;
	}

	public void setManualadjust(String manualadjust) {
		this.manualadjust = manualadjust;
	}

	@Override
	public String toString() {
		return "INSBQuoteconfig [agreementid=" + agreementid
				+ ", autorulequote=" + autorulequote + ", autojinglingquote="
				+ autojinglingquote + ", autoediquote=" + autoediquote
				+ ", manualrecord=" + manualrecord + ", manuarulelrecord="
				+ manuarulelrecord + ", manualadjust=" + manualadjust + "]";
	}

	
}