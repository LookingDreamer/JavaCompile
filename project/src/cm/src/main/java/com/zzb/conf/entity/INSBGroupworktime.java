package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBGroupworktime extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 业管群组id
	 */
	private String businessgroupid;

	/**
	 * 工作日起
	 */
	private String startworkdate;

	/**
	 * 工作日止
	 */
	private String endworkdate;

	/**
	 * 工作时间起
	 */
	private String startworktime;

	/**
	 * 工作时间止
	 */
	private String endworktime;

	public String getBusinessgroupid() {
		return businessgroupid;
	}

	public void setBusinessgroupid(String businessgroupid) {
		this.businessgroupid = businessgroupid;
	}

	public String getStartworkdate() {
		return startworkdate;
	}

	public void setStartworkdate(String startworkdate) {
		this.startworkdate = startworkdate;
	}

	public String getEndworkdate() {
		return endworkdate;
	}

	public void setEndworkdate(String endworkdate) {
		this.endworkdate = endworkdate;
	}

	public String getStartworktime() {
		return startworktime;
	}

	public void setStartworktime(String startworktime) {
		this.startworktime = startworktime;
	}

	public String getEndworktime() {
		return endworktime;
	}

	public void setEndworktime(String endworktime) {
		this.endworktime = endworktime;
	}

}