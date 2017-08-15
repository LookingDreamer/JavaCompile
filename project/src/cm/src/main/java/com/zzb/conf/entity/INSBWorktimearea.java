package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBWorktimearea extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String operater;

	/**
	 * 
	 */
	private String insbworktimeid;

	/**
	 * 
	 */
	private String workdaystart;

	/**
	 * 
	 */
	private String workdayend;

	/**
	 * 
	 */
	private String daytimestart;

	/**
	 * 
	 */
	private String daytimeend;

	/**
	 * 
	 */
	private String isremindtime;

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getInsbworktimeid() {
		return insbworktimeid;
	}

	public void setInsbworktimeid(String insbworktimeid) {
		this.insbworktimeid = insbworktimeid;
	}

	public String getWorkdaystart() {
		return workdaystart;
	}

	public void setWorkdaystart(String workdaystart) {
		this.workdaystart = workdaystart;
	}

	public String getWorkdayend() {
		return workdayend;
	}

	public void setWorkdayend(String workdayend) {
		this.workdayend = workdayend;
	}

	public String getDaytimestart() {
		return daytimestart;
	}

	public void setDaytimestart(String daytimestart) {
		this.daytimestart = daytimestart;
	}

	public String getDaytimeend() {
		return daytimeend;
	}

	public void setDaytimeend(String daytimeend) {
		this.daytimeend = daytimeend;
	}

	public String getIsremindtime() {
		return isremindtime;
	}

	public void setIsremindtime(String isremindtime) {
		this.isremindtime = isremindtime;
	}

}