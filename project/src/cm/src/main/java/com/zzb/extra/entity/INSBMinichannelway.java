package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBMinichannelway extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道编码
	 */
	private String channelcode;

	/**
	 * 渠道推广途径编码
	 */
	private Integer waycode;

	/**
	 * 渠道推广途径名称
	 */
	private String wayname;

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public Integer getWaycode() {
		return waycode;
	}

	public void setWaycode(Integer waycode) {
		this.waycode = waycode;
	}

	public String getWayname() {
		return wayname;
	}

	public void setWayname(String wayname) {
		this.wayname = wayname;
	}

}