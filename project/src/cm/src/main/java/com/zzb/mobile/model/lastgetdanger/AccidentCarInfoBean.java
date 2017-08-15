package com.zzb.mobile.model.lastgetdanger;
public class AccidentCarInfoBean {
	String vin;   //vin码（必传）
	String carBrandName;//品牌型号（必传）
	String engineNum;  //发动机号（必传）
	String firstRegDate;  //初登日期（必传）
	String plateNum;
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCarBrandName() {
		return carBrandName;
	}
	public void setCarBrandName(String carBrandName) {
		this.carBrandName = carBrandName;
	}
	public String getEngineNum() {
		return engineNum;
	}
	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	public String getFirstRegDate() {
		return firstRegDate;
	}
	public void setFirstRegDate(String firstRegDate) {
		this.firstRegDate = firstRegDate;
	}          
	
}
