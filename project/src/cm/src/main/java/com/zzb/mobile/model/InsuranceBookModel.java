package com.zzb.mobile.model;

public class InsuranceBookModel {

	/**
	 * 品牌型号
	 */
	private String modelCode;
	/**
	 * 车辆识别代号
	 */
	private String vin;
	/**
	 * 发动机号
	 */
	private String engineNo;
	/**
	 * 是否过户车  0不 是  1 是
	 */
	private String chgOwnerFlag;
	/**
	 * 过户时间
	 */
	private String chgOwnerDate; 
	/**
	 * 车辆初次登录日期
	 */
	private String firstRegisterDate;
	/**
	 * 投保书标记
	 */
	private String webpagekey;
	/**
	 * 行驶证图片路径
	 */
	private String drivinglicense;
	/**
	 * 行驶证图片id
	 */
	private String fileid;
	/**
	 * 所属性质 0:个人用车, 1:企业用车,2:机关团体用车
	 */
	private String institutionType;
	/**
	 * 车辆性质,使用性质
	 */
	private String useProperty;
	/**
	 * 排量
	 */
	private double displacement;
	/**
	 * 核定载人数
	 */
	private int approvedLoad;
	/**
	 * 车价选择，0最低，1最高，2自定义
	 */
	private String rulePriceProvideType;
	/**
	 * 核定载质量
	 */
	private Double tonnage;
	/**
	 * 整备质量
	 */
	private Double wholeWeight;
	/**
	 * 制定车价   车价选择2的时候输入的值
	 */
	private Double customPrice;
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
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
	public String getFirstRegisterDate() {
		return firstRegisterDate;
	}
	public void setFirstRegisterDate(String firstRegisterDate) {
		this.firstRegisterDate = firstRegisterDate;
	}
	public String getWebpagekey() {
		return webpagekey;
	}
	public void setWebpagekey(String webpagekey) {
		this.webpagekey = webpagekey;
	}
	public String getDrivinglicense() {
		return drivinglicense;
	}
	public void setDrivinglicense(String drivinglicense) {
		this.drivinglicense = drivinglicense;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
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
	public double getDisplacement() {
		return displacement;
	}
	public void setDisplacement(double displacement) {
		this.displacement = displacement;
	}
	public int getApprovedLoad() {
		return approvedLoad;
	}
	public void setApprovedLoad(int approvedLoad) {
		this.approvedLoad = approvedLoad;
	}
	public String getRulePriceProvideType() {
		return rulePriceProvideType;
	}
	public void setRulePriceProvideType(String rulePriceProvideType) {
		this.rulePriceProvideType = rulePriceProvideType;
	}
	public Double getTonnage() {
		return tonnage;
	}
	public void setTonnage(Double tonnage) {
		this.tonnage = tonnage;
	}
	public Double getWholeWeight() {
		return wholeWeight;
	}
	public void setWholeWeight(Double wholeWeight) {
		this.wholeWeight = wholeWeight;
	}
	public Double getCustomPrice() {
		return customPrice;
	}
	public void setCustomPrice(Double customPrice) {
		this.customPrice = customPrice;
	}
	
}
