package com.zzb.mobile.model;

import org.springframework.beans.factory.annotation.Required;

import net.sf.json.JSONObject;

public class PayParam {
	private String jobNum;
	private JSONObject payJsonStr;
	private String bizId;//支付询价号
	private String bankCardInfoId;
	private String password;
	private String bizTransactionId;//支付流水号
	/** 是否核保查询，是：true  表示只是通知精灵去获取二维码, 否：false 正常的支付流程 */
	private String isInsureQuery;
	/** 是否承保查询，是：true  表示需要通知精灵进行承保查询, 否：false 不需要 */
	private Boolean isUnderwriteQuote;
	
	public String getBizTransactionId() {
		return bizTransactionId;
	}
	public void setBizTransactionId(String bizTransactionId) {
		this.bizTransactionId = bizTransactionId;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	public JSONObject getPayJsonStr() {
		return payJsonStr;
	}
	public void setPayJsonStr(JSONObject payJsonStr) {
		this.payJsonStr = payJsonStr;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBankCardInfoId() {
		return bankCardInfoId;
	}
	public void setBankCardInfoId(String bankCardInfoId) {
		this.bankCardInfoId = bankCardInfoId;
	}

	public String getIsInsureQuery() {
		return isInsureQuery;
	}

	public void setIsInsureQuery(String isInsureQuery) {
		this.isInsureQuery = isInsureQuery;
	}

	public Boolean getIsUnderwriteQuote() {
		return isUnderwriteQuote;
	}

	public void setIsUnderwriteQuote(Boolean isUnderwriteQuote) {
		this.isUnderwriteQuote = isUnderwriteQuote;
	}
}
