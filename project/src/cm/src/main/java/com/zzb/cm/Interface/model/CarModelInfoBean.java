package com.zzb.cm.Interface.model;


public class CarModelInfoBean {
	
	private Double analogyprice;
	private Double analogytaxprice;
	private String brandname;
	private String configname;
	private String displacement;
	private String factoryname;
	private String familyname;
	private String fullweight;
	private String gearbox;
	private String id;
	private String jqvehicletype;
	private String jqvehicletypename;
	private String maketdate;
	private String manufacturer;
	private String modelLoads;
	private Double price;
	private String seat;
	private String syvehicletype;
	private String syvehicletypename;
	private Double taxprice;
	private String vehicleId;
	private String vehiclecode;
	private String vehiclename;
	private String vehicletype;
	private String vehicletypecode;
	private String yearstyle;
	private String jycode;
	private String rbcode;
	
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public String getConfigname() {
		return configname;
	}
	public void setConfigname(String configname) {
		this.configname = configname;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public String getFactoryname() {
		return factoryname;
	}
	public void setFactoryname(String factoryname) {
		this.factoryname = factoryname;
	}
	public String getFamilyname() {
		return familyname;
	}
	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}
	public String getFullweight() {
		return fullweight;
	}
	public void setFullweight(String fullweight) {
		this.fullweight = fullweight;
	}
	public String getGearbox() {
		return gearbox;
	}
	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJqvehicletype() {
		return jqvehicletype;
	}
	public void setJqvehicletype(String jqvehicletype) {
		this.jqvehicletype = jqvehicletype;
	}
	public String getJqvehicletypename() {
		return jqvehicletypename;
	}
	public void setJqvehicletypename(String jqvehicletypename) {
		this.jqvehicletypename = jqvehicletypename;
	}
	public String getMaketdate() {
		return maketdate;
	}
	public void setMaketdate(String maketdate) {
		this.maketdate = maketdate;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModelLoads() {
		return modelLoads;
	}
	public void setModelLoads(String modelLoads) {
		this.modelLoads = modelLoads;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getSyvehicletype() {
		return syvehicletype;
	}
	public void setSyvehicletype(String syvehicletype) {
		this.syvehicletype = syvehicletype;
	}
	public String getSyvehicletypename() {
		return syvehicletypename;
	}
	public void setSyvehicletypename(String syvehicletypename) {
		this.syvehicletypename = syvehicletypename;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehiclecode() {
		return vehiclecode;
	}
	public void setVehiclecode(String vehiclecode) {
		this.vehiclecode = vehiclecode;
	}
	public String getVehiclename() {
		return vehiclename;
	}
	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getVehicletypecode() {
		return vehicletypecode;
	}
	public void setVehicletypecode(String vehicletypecode) {
		this.vehicletypecode = vehicletypecode;
	}
	public String getYearstyle() {
		return yearstyle;
	}
	public void setYearstyle(String yearstyle) {
		this.yearstyle = yearstyle;
	}
	public Double getAnalogyprice() {
		return analogyprice;
	}
	public void setAnalogyprice(Double analogyprice) {
		this.analogyprice = analogyprice;
	}
	public Double getAnalogytaxprice() {
		return analogytaxprice;
	}
	public void setAnalogytaxprice(Double analogytaxprice) {
		this.analogytaxprice = analogytaxprice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTaxprice() {
		return taxprice;
	}
	public void setTaxprice(Double taxprice) {
		this.taxprice = taxprice;
	}
	public String getJycode() {
		return jycode;
	}
	public void setJycode(String jycode) {
		this.jycode = jycode;
	}
	public String getRbcode() {
		return rbcode;
	}
	public void setRbcode(String rbcode) {
		this.rbcode = rbcode;
	}
	
	@Override
	public String toString() {
		return "车型名称=" + brandname + ",含税价="
					+ taxprice+",不含税价=" + price + ",含税类比价=" + analogytaxprice
					+",不含税类比价=" + analogyprice+",上市日期=" + maketdate +",vehicleId=" + vehicleId + ",排气量=" + displacement + ",核定载质量=" + fullweight + ",座位数=" + seat;
	}
}
