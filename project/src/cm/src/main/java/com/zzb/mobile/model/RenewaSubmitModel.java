package com.zzb.mobile.model;

import java.util.List;

public class RenewaSubmitModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 行驶区域是否与上一年一致（0一致 1 不一致）
	 */
	private String driverway;
	/**
	 * 行驶区域  1:出入境、0:境内、2:省内、3:场内、4:固定线路
	 */
	private String drivingRegion;
	/**
	 * 指定驾驶人 0与上一年一致 1不一致
	 */
	private String driversflag;
	/**
	 * 指定驾驶人列表
	 */
	private List<CarDriver> drivers;

	public String getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public String getDriverway() {
		return driverway;
	}

	public void setDriverway(String driverway) {
		this.driverway = driverway;
	}

	public String getDrivingRegion() {
		return drivingRegion;
	}

	public void setDrivingRegion(String drivingRegion) {
		this.drivingRegion = drivingRegion;
	}

	public String getDriversflag() {
		return driversflag;
	}

	public void setDriversflag(String driversflag) {
		this.driversflag = driversflag;
	}

	public List<CarDriver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<CarDriver> drivers) {
		this.drivers = drivers;
	}
	
}
