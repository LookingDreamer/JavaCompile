package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("vehicleInfo")
public class VehicleInfo {

	/**
	 * price
	 */
	@XStreamAlias("price")
	private Double price;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public VehicleInfo() {
		super();
	}
	
}
