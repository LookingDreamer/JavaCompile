package com.zzb.cm.Interface.entity.car.model; /**
 * 
 */

import java.io.Serializable;

/**
 * 渠道信息描述，包含渠道能力描述
 * @author austinChen
 * created at 2015年6月12日 下午1:59:12
 */
public class ChannelInfo  implements Serializable {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	
	//private Integer 
	
//	{
//	    "appKey": "testChannel",
//	    "channel": "mobile",
//	    "callBack": "",
//	    "version": "V1.0",
//	    "appId": "testChannel"
//	  }
	
	/**
	 * 渠道接入商的应用appId
	 */
	private String appId="";
	/**
	 * 渠道接入商的应用appKey
	 */
	private String appKey="";
	/**
	 * 渠道的接入方式
	 */
	private String channel="";
	/**
	 * 报价，核保，承保，支付结果通知回调渠道的url
	 */
	private String callBack="";
	/**
	 * 协议版本号
	 */
	private String version="V1.0";
	
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCallBack() {
		return callBack;
	}
	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}
	
	

}
