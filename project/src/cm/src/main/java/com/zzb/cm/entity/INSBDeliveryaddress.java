package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBDeliveryaddress extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 车主姓名
	 */
	private String ownername;

	/**
	 * 车主证件号码
	 */
	private String owneridcardno;

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
	 * 主任务id
	 *
	 */
	private String taskid; 

	/*
	 * 	配送方式
	 *
	 */
	private String delivetype;

	/**
	 * 渠道ID
	 */
	private String channelid;
	/**
	 * 渠道用户ID
	 */
	private String channeluserid;

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getOwneridcardno() {
		return owneridcardno;
	}

	public void setOwneridcardno(String owneridcardno) {
		this.owneridcardno = owneridcardno;
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDelivetype() {
		return delivetype;
	}

	public void setDelivetype(String delivetype) {
		this.delivetype = delivetype;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getChanneluserid() {
		return channeluserid;
	}

	public void setChanneluserid(String channeluserid) {
		this.channeluserid = channeluserid;
	}
}