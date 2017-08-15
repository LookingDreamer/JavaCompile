package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("vehicleInfo")
public class CarBasicInfo {

	/**
	 * 保网型号ID，之前根据车两品牌查询车辆型号中有返回
	 */
	@XStreamAlias("modelId")
	private String modelId;
	/**
	 * 车主姓名
	 */
	@XStreamAlias("owner")
	private String owner;
	/**
	 * 品牌
	 */
	@XStreamAlias("modelCode")
	private String modelCode;
	/**
	 * 车重
	 */
	@XStreamAlias("totalVehicleWeight")
	private String totalVehicleWeight;
	/**
	 * 车牌
	 */
	@XStreamAlias("plateNumber")
	private String plateNumber;
	/**
	 * 车辆初次登录日期
	 */
	@XStreamAlias("firstRegisterDate")
	private String firstRegisterDate;
	/**
	 * 车辆识别代号
	 */
	@XStreamAlias("vin")
	private String vin;
	/**
	 * 发动机号
	 */
	@XStreamAlias("engineNo")
	private String engineNo;
	/**
	 * 是否新车
	 */
	@XStreamAlias("newVehicleFlag")
	private String newVehicleFlag;
	/**
	 * 是否过户车 true 是  false 不是
	 */
	@XStreamAlias("chgOwnerFlag")
	private String chgOwnerFlag;
	/**
	 * 过户时间
	 */
	@XStreamAlias("chgOwnerDate")
	private String chgOwnerDate;
	/**
	 * 所属性质
	 */
	@XStreamAlias("institutionType")
	private String institutionType;
	/**
	 * 车辆性质
	 */
	@XStreamAlias("useProperty")
	private String useProperty;
	/**
	 * 品牌型号
	 */
	@XStreamAlias("licenseModelCode")
	private String licenseModelCode;
	/**
	 * 备注
	 */
	@XStreamAlias("modelDesc")
	private String modelDesc;
	/**
	 * 核定载重人
	 */
	@XStreamAlias("ratedPassengerCapacity")
	private String ratedPassengerCapacity;
	/**
	 * 排量
	 */
	@XStreamAlias("displacement")
	private String displacement;
	/**
	 * 整备质量
	 */
	@XStreamAlias("wholeWeight")
	private String wholeWeight;
	/**
	 * 核定载重量
	 */
	@XStreamAlias("tonnage")
	private String tonnage;
	/**
	 * 车价选择，0，1，2
	 */
	@XStreamAlias("rulePriceProvideType")
	private String rulePriceProvideType;
	/**
	 * 如果车价选择是2，才需要此参数，自定义购置价
	 */
	@XStreamAlias("carRulePrice")
	private String carRulePrice;
	
	/**
	 * 
	 */
	@XStreamAlias("userReplacementValue")
	private String userReplacementValue;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getTotalVehicleWeight() {
		return totalVehicleWeight;
	}

	public void setTotalVehicleWeight(String totalVehicleWeight) {
		this.totalVehicleWeight = totalVehicleWeight;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getFirstRegisterDate() {
		return firstRegisterDate;
	}

	public void setFirstRegisterDate(String firstRegisterDate) {
		this.firstRegisterDate = firstRegisterDate;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getNewVehicleFlag() {
		return newVehicleFlag;
	}

	public void setNewVehicleFlag(String newVehicleFlag) {
		this.newVehicleFlag = newVehicleFlag;
	}

	public String getChgOwnerFlag() {
		return chgOwnerFlag;
	}

	public void setChgOwnerFlag(String chgOwnerFlag) {
		this.chgOwnerFlag = chgOwnerFlag;
	}

	public String getChgOwnerDate() {
		return chgOwnerDate;
	}

	public void setChgOwnerDate(String chgOwnerDate) {
		this.chgOwnerDate = chgOwnerDate;
	}

	public String getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(String institutionType) {
		this.institutionType = institutionType;
	}

	public String getUseProperty() {
		return useProperty;
	}

	public void setUseProperty(String useProperty) {
		this.useProperty = useProperty;
	}

	public String getLicenseModelCode() {
		return licenseModelCode;
	}

	public void setLicenseModelCode(String licenseModelCode) {
		this.licenseModelCode = licenseModelCode;
	}

	public String getModelDesc() {
		return modelDesc;
	}

	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}

	public String getRatedPassengerCapacity() {
		return ratedPassengerCapacity;
	}

	public void setRatedPassengerCapacity(String ratedPassengerCapacity) {
		this.ratedPassengerCapacity = ratedPassengerCapacity;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getWholeWeight() {
		return wholeWeight;
	}

	public void setWholeWeight(String wholeWeight) {
		this.wholeWeight = wholeWeight;
	}

	public String getTonnage() {
		return tonnage;
	}

	public void setTonnage(String tonnage) {
		this.tonnage = tonnage;
	}

	public String getRulePriceProvideType() {
		return rulePriceProvideType;
	}

	public void setRulePriceProvideType(String rulePriceProvideType) {
		this.rulePriceProvideType = rulePriceProvideType;
	}

	public String getCarRulePrice() {
		return carRulePrice;
	}

	public void setCarRulePrice(String carRulePrice) {
		this.carRulePrice = carRulePrice;
	}

	public String getUserReplacementValue() {
		return userReplacementValue;
	}

	public void setUserReplacementValue(String userReplacementValue) {
		this.userReplacementValue = userReplacementValue;
	}
	
}
