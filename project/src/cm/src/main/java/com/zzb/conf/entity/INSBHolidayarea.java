package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBHolidayarea extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;


	/**
	 * 
	 */
	private String insbworktimeid;

	/**
	 * 
	 */
	private String datestart;

	/**
	 * 
	 */
	private String dateend;

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
	private String isonduty;


	public String getInsbworktimeid() {
		return insbworktimeid;
	}

	public void setInsbworktimeid(String insbworktimeid) {
		this.insbworktimeid = insbworktimeid;
	}

	public String getDatestart() {
		return datestart;
	}

	public void setDatestart(String datestart) {
		this.datestart = datestart;
	}

	public String getDateend() {
		return dateend;
	}

	public void setDateend(String dateend) {
		this.dateend = dateend;
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

	public String getIsonduty() {
		return isonduty;
	}

	public void setIsonduty(String isonduty) {
		this.isonduty = isonduty;
	}

}