package com.zzb.cm.Interface.entity.car.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 渠道登陆的Session对象
 * @author austinChen
 * created at 2015年6月12日 下午1:43:12
 */
public class AppSession implements Serializable{

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -3243866955835268868L;
	
	
	private ChannelInfo channel;
	/**
	 * 会话ID，UUID
	 */
	private String sid=UUID.randomUUID().toString();
	/**
	 * 备用的字段。对sid和appId,使用appKey进行加密计算
	 */
	private String token="";
	private long startTime=0L;
	
	

	public ChannelInfo getChannel() {
		return channel;
	}

	public void setChannel(ChannelInfo channel) {
		this.channel = channel;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public AppSession(String sid,String token,ChannelInfo channel)
	{
		this.sid=sid;
		this.token=token;
		this.channel=channel;
	}

}
