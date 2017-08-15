package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgentDeliveryaddress extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 收件人姓名
	 */
	private String recipientname;

	/**
	 * 收件人手机号
	 */
	private String recipientmobilephone;

	/**
	 * 收件人省份
	 */
	private String recipientprovince;

	/**
	 * 收件人城市
	 */
	private String recipientcity;

	/**
	 * 收件人地区
	 */
	private String recipientarea;

	/**
	 * 收件人详细地址
	 */
	private String recipientaddress;

	/**
	 * 邮编
	 */
	private String zip;

	/*
	 * 代理人id
	 *
	 */
	private String agentid;

	/*
	 * 	配送方式
	 *
	 */
	private String delivetype;

	/** 默认配送地址 */
	private String isdefault = "0";

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getRecipientname() {
		return recipientname;
	}

	public void setRecipientname(String recipientname) {
		this.recipientname = recipientname;
	}

	public String getRecipientmobilephone() {
		return recipientmobilephone;
	}

	public void setRecipientmobilephone(String recipientmobilephone) {
		this.recipientmobilephone = recipientmobilephone;
	}

	public String getRecipientprovince() {
		return recipientprovince;
	}

	public void setRecipientprovince(String recipientprovince) {
		this.recipientprovince = recipientprovince;
	}

	public String getRecipientcity() {
		return recipientcity;
	}

	public void setRecipientcity(String recipientcity) {
		this.recipientcity = recipientcity;
	}

	public String getRecipientarea() {
		return recipientarea;
	}

	public void setRecipientarea(String recipientarea) {
		this.recipientarea = recipientarea;
	}

	public String getRecipientaddress() {
		return recipientaddress;
	}

	public void setRecipientaddress(String recipientaddress) {
		this.recipientaddress = recipientaddress;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getDelivetype() {
		return delivetype;
	}

	public void setDelivetype(String delivetype) {
		this.delivetype = delivetype;
	}
}