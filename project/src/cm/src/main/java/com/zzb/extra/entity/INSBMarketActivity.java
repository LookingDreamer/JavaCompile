package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class INSBMarketActivity extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 活动编号 
	 */
	private String activitycode;
	
	/**
	 * 临时编号
	 */
	private Integer tmpcode;

	/**
	 * 活动名称
	 */
	private String activityname;

	/**
	 * 活动类型
	 */
	private String activitytype;

	/**
	 * 参与限制
	 */
	private Integer limited;

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
	 * 活动状态 refresh
	 */
	private String status;

	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	public String getActivitytype() {
		return activitytype;
	}

	public void setActivitytype(String activitytype) {
		this.activitytype = activitytype;
	}

	public Integer getLimited() {
		return limited;
	}

	public void setLimited(Integer limited) {
		this.limited = limited;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivitycode() {
		return activitycode;
	}

	public void setActivitycode(String activitycode) {
		this.activitycode = activitycode;
	}

	public Integer getTmpcode() {
		return tmpcode;
	}

	public void setTmpcode(Integer tmpcode) {
		this.tmpcode = tmpcode;
	}
	
	
}