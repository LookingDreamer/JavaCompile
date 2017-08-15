package com.zzb.chn.bean;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayInfo {
	private String channelId; // payeco、alipay、tenpay、yeepay、wxpay
	private String channelName;
	private String payType; // visual、mobile、web、offline、voice
	private String payTypeDesc;
	private String bizTransactionId; // 支付流水号
	private String paySource; // 支付来源 业务线.渠道类型(渠道、直客、代理).渠道
	private Integer amount; // 支付金额
	private List<Channel> channelList;
	private String code;
	private String msg;
	private String insSerialNo;
	private String paySerialNo; // 支付公司的支付流水号
	private String payResult;
	private String quickposNo;
	private String orderState;
	private String reference;
	private String payResultDesc;
	private String transDate;
	private String validTime;
	private String notifyUrl;
	private String notifyType;
	private String notifyTime;
	private String agentOrg; // 代理机构
	private String insOrg; // 保险公司机构
	private String bizId;
	private String payUrl; // 前端需要跳转的第三方支付公司的支付地址
	private Map<String, String> channelParam;
	private String areaCode;
	private String ipAddress;
	private String sign; // MD5、SHA1、SHA256、HMAC
	private String signType; // 签名类型
	private String nonceStr; // 随机串
	private String qrcodeUrl;
	private String qrcodeContent;
	private String para;
	private String retUrl; // 商品URL
	private String taskId;
	private String description;
	private String productName;
	private String prvId;
	private String refundId;
	private String payData;
	
	

	public String getPayData() {
		return payData;
	}

	public void setPayData(String payData) {
		this.payData = payData;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public String getPrvId() {
		return prvId;
	}

	public void setPrvId(String prvId) {
		this.prvId = prvId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getQuickposNo() {
		return quickposNo;
	}

	public void setQuickposNo(String quickposNo) {
		this.quickposNo = quickposNo;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getQrcodeContent() {
		return qrcodeContent;
	}

	public void setQrcodeContent(String qrcodeContent) {
		this.qrcodeContent = qrcodeContent;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getAgentOrg() {
		return agentOrg;
	}

	public void setAgentOrg(String agentOrg) {
		this.agentOrg = agentOrg;
	}

	public String getInsOrg() {
		return insOrg;
	}

	public void setInsOrg(String insOrg) {
		this.insOrg = insOrg;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getInsSerialNo() {
		return insSerialNo;
	}

	public void setInsSerialNo(String insSerialNo) {
		this.insSerialNo = insSerialNo;
	}

	public String getPaySerialNo() {
		return paySerialNo;
	}

	public void setPaySerialNo(String paySerialNo) {
		this.paySerialNo = paySerialNo;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getPayResultDesc() {
		return payResultDesc;
	}

	public void setPayResultDesc(String payResultDesc) {
		this.payResultDesc = payResultDesc;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Channel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPaySource() {
		return paySource;
	}

	public void setPaySource(String paySource) {
		this.paySource = paySource;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeDesc() {
		return payTypeDesc;
	}

	public void setPayTypeDesc(String payTypeDesc) {
		this.payTypeDesc = payTypeDesc;
	}

	public String getBizTransactionId() {
		return bizTransactionId;
	}

	public void setBizTransactionId(String bizTransactionId) {
		this.bizTransactionId = bizTransactionId;
	}

	public Map<String, String> getChannelParam() {
		return channelParam;
	}

	public void setChannelParam(Map<String, String> channelParam) {
		this.channelParam = channelParam;
	}
}
