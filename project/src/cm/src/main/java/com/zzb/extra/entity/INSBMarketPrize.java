package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class INSBMarketPrize extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 奖品名称
	 */
	private String prizename;

	/**
	 * 奖品类型
	 */
	private String prizetype;

	/**
	 * 数额
	 */
	private Double counts;

	/**
	 * 生效时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date effectivetime;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date terminaltime;

	/**
	 * 生效天数
	 */
	private Integer effectivedays;

	/**
	 * 奖品状态
	 */
	private String status;

	/**
	 * 使用状态 refresh
	 */
	private String autouse;

	public String getAutouse() {
		return autouse;
	}

	public void setAutouse(String autouse) {
		this.autouse = autouse;
	}

	public String getPrizename() {
		return prizename;
	}

	public void setPrizename(String prizename) {
		this.prizename = prizename;
	}

	public String getPrizetype() {
		return prizetype;
	}

	public void setPrizetype(String prizetype) {
		this.prizetype = prizetype;
	}

	public Double getCounts() {
		return counts;
	}

	public void setCounts(Double counts) {
		this.counts = counts;
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

	public Integer getEffectivedays() {
		return effectivedays;
	}

	public void setEffectivedays(Integer effectivedays) {
		this.effectivedays = effectivedays;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}