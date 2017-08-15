package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 车型信息bean
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CarModelInfoBean {
	private String analogyPrice;
	private String analogyTaxPrice;
	private String brandName;
	private String configName;
	private String displacement;
	private String fullWeight;
	private String maketDate;
	private String producingArea;
	private String modelLoads;
	private String price;
	private String seat;
	private String taxPrice;
	private String vehicleCode;
	private String vehicleName;
	private String vehicleId;
	private String yearStyle;
	private String rulePriceProvideType;
	private String factoryName;
	private String familyName;
	private String gearbox;
	private String policycarprice; //投保车价
	private String yearPrice; //年款，不小心和yearStyle冗余了，yearPrice只在回调返回数据时用到了
	private String vehicleTypeName; //车辆种类
	private String carPrice;
	
	public String getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(String carPrice) {
		this.carPrice = carPrice;
	}

	public String getYearPrice() {
		return yearPrice;
	}

	public void setYearPrice(String yearPrice) {
		this.yearPrice = yearPrice;
	}

	public String getVehicleTypeName() {
		return vehicleTypeName;
	}

	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGearbox() {
		return gearbox;
	}

	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getAnalogyPrice() {
		return analogyPrice;
	}

	public void setAnalogyPrice(String analogyPrice) {
		this.analogyPrice = analogyPrice;
	}

	public String getAnalogyTaxPrice() {
		return analogyTaxPrice;
	}

	public void setAnalogyTaxPrice(String analogyTaxPrice) {
		this.analogyTaxPrice = analogyTaxPrice;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(String fullWeight) {
		this.fullWeight = fullWeight;
	}

	public String getMaketDate() {
		return maketDate;
	}

	public void setMaketDate(String maketDate) {
		this.maketDate = maketDate;
	}

	public String getProducingArea() {
		return producingArea;
	}

	public void setProducingArea(String producingArea) {
		this.producingArea = producingArea;
	}

	public String getModelLoads() {
		return modelLoads;
	}

	public void setModelLoads(String modelLoads) {
		this.modelLoads = modelLoads;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	
	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getYearStyle() {
		return yearStyle;
	}

	public void setYearStyle(String yearStyle) {
		this.yearStyle = yearStyle;
	}

	public String getRulePriceProvideType() {
		return rulePriceProvideType;
	}

	public void setRulePriceProvideType(String rulePriceProvideType) {
		this.rulePriceProvideType = rulePriceProvideType;
	}

	public String getPolicycarprice() {
		return policycarprice;
	}

	public void setPolicycarprice(String policycarprice) {
		this.policycarprice = policycarprice;
	}
	
}
