package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CarInfoBean extends CarModelInfoBean {
	private String carLicenseNo;
	private String isNew;
	private String vinCode;
	private String engineNo;
	private String carProperty;
	private String registDate;
	private String purpose;
	private String rulePriceProvideType;
	private String customPrice;
	private String isTransferCar;
	private String transferDate;
	private String property;
	private String drivingArea;
	
	
	public String getDrivingArea() {
		return drivingArea;
	}

	public void setDrivingArea(String drivingArea) {
		this.drivingArea = drivingArea;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getIsTransferCar() {
		return isTransferCar;
	}

	public void setIsTransferCar(String isTransferCar) {
		this.isTransferCar = isTransferCar;
	}

	public String getRulePriceProvideType() {
		return rulePriceProvideType;
	}

	public void setRulePriceProvideType(String rulePriceProvideType) {
		this.rulePriceProvideType = rulePriceProvideType;
	}

	public String getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(String customPrice) {
		this.customPrice = customPrice;
	}

	public String getCarLicenseNo() {
		return carLicenseNo;
	}

	public void setCarLicenseNo(String carLicenseNo) {
		this.carLicenseNo = carLicenseNo;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getVinCode() {
		return vinCode;
	}

	public void setVinCode(String vinCode) {
		this.vinCode = vinCode;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getCarProperty() {
		return carProperty;
	}

	public void setCarProperty(String carProperty) {
		this.carProperty = carProperty;
	}

	public String getRegistDate() {
		return registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

}
