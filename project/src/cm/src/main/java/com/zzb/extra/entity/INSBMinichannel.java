package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class INSBMinichannel extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道编码
	 */
	private String channelcode;

	/**
	 * 渠道名称
	 */
	private String channelname;

	/**
	 * 渠道推广途径数量
	 */
	private Integer waynum;

	/**
	 * 临时编码
	 */
	private Integer tempcode;

	/**
	 * 渠道合作截止时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date terminaldate;

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public Integer getWaynum() {
		return waynum;
	}

	public void setWaynum(Integer waynum) {
		this.waynum = waynum;
	}

	public Integer getTempcode() {
		return tempcode;
	}

	public void setTempcode(Integer tempcode) {
		this.tempcode = tempcode;
	}

	public Date getTerminaldate() {
		return terminaldate;
	}

	public void setTerminaldate(Date terminaldate) {
		this.terminaldate = terminaldate;
	}

}