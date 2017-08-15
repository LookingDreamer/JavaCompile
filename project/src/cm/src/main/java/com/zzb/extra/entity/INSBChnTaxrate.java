package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBChnTaxrate extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道id
	 */
	private String channelid;

	/**
	 * 生效时间
	 */
	private Date effectivetime;

	/**
	 * 失效时间
	 */
	private Date terminaltime;

	/**
	 * 税率
	 */
	private Double taxrate;

	/**
	 * 税率状态 1-已启用，2-已关闭，3-待审核 
	 */
	private String status;

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public Date getEffectivetime() {
		return effectivetime;
	}

	public void setEffectivetime(Date effectivetime) {
		this.effectivetime = effectivetime;
	}

	public Date getTerminaltime() {
		return terminaltime;
	}

	public void setTerminaltime(Date terminaltime) {
		this.terminaltime = terminaltime;
	}

	public Double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}