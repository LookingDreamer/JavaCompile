package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBBatch extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 批次号
	 */
	private String batchacode;

	/**
	 * 渠道编码
	 */
	private String channelcode;

	public String getBatchacode() {
		return batchacode;
	}

	public void setBatchacode(String batchacode) {
		this.batchacode = batchacode;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

}