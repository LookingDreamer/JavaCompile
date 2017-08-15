package com.zzb.app.model.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("drivers")
public class DriversList {
	
	@XStreamImplicit(itemFieldName="row")
	private List<DriverInfo> driverInfos;

	public List<DriverInfo> getDriverInfos() {
		return driverInfos;
	}

	public void setDriverInfos(List<DriverInfo> driverInfos) {
		this.driverInfos = driverInfos;
	}
	
	
}
