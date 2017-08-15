package com.zzb.chn.bean;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 供应商信息bean
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ProviderBean {
	private String prvId;
	private String prvCode;
	private String prvStateCode;
	private String prvName;
	private String businessType;
	private String totalPrice;
	private String payValidTime;
	private String quoteValidTime;
	private String msg;
	private List<ImageInfoBean> imageInfos; //影像信息
	private String msgType; //msg的类型，00-[成功]非工作时间提醒；01-[失败]上传影像提醒；
	
	public List<ImageInfoBean> getImageInfos() {
		return imageInfos;
	}

	public void setImageInfos(List<ImageInfoBean> imageInfos) {
		this.imageInfos = imageInfos;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getPrvId() {
		return prvId;
	}

	public void setPrvId(String prvId) {
		this.prvId = prvId;
	}

	public String getPrvCode() {
		return prvCode;
	}

	public void setPrvCode(String prvCode) {
		this.prvCode = prvCode;
	}

	public String getPrvName() {
		return prvName;
	}

	public void setPrvName(String prvName) {
		this.prvName = prvName;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPrvStateCode() {
		return prvStateCode;
	}

	public void setPrvStateCode(String prvStateCode) {
		this.prvStateCode = prvStateCode;
	}

	public String getPayValidTime() {
		return payValidTime;
	}

	public void setPayValidTime(String payValidTime) {
		this.payValidTime = payValidTime;
	}

	public String getQuoteValidTime() {
		return quoteValidTime;
	}

	public void setQuoteValidTime(String quoteValidTime) {
		this.quoteValidTime = quoteValidTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
