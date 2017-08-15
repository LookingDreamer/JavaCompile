package com.zzb.mobile.model;

import java.util.List;

public class DriversInfoModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 驾驶人列表
	 */
	private List<CarDriver> drivers;

	public String getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public List<CarDriver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<CarDriver> drivers) {
		this.drivers = drivers;
	}
	
	
}
