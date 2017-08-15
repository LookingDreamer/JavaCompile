package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("recentInsure")
public class RecentInsure {

	/**
	 * 上家商业承保公司ID
	 */
	@XStreamAlias("lastComId")
	private String lastComId;
	/**
	 * 平均行驶里程
	 */
	@XStreamAlias("avkt")
	private String avkt;
	/**
	 * 行驶区域, 1:"出入境"、2:"境内"、3:"省内"、4:"场内"、5:"固定线路"
	 */
	@XStreamAlias("drivingRegion")
	private String drivingRegion;

	public String getLastComId() {
		return lastComId;
	}
	public void setLastComId(String lastComId) {
		this.lastComId = lastComId;
	}
	public String getAvkt() {
		return avkt;
	}
	public void setAvkt(String avkt) {
		this.avkt = avkt;
	}
	public String getDrivingRegion() {
		return drivingRegion;
	}
	public void setDrivingRegion(String drivingRegion) {
		this.drivingRegion = drivingRegion;
	}
	
}
