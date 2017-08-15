package com.zzb.mobile.model;

public class CarModelInfoModel {
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 品牌型号
	 */
	private String modelCode;
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
	 * 所属性质 0:个人用车, 1:企业用车,2:机关团体用车
	 */
	private String institutionType;
	/**
	 * 车辆性质,使用性质
	 */
	private String useProperty;
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
	/**
	 * 投保书标记
	 */
	private String webpagekey;
	
	public String getWebpagekey() {
		return webpagekey;
	}
	public void setWebpagekey(String webpagekey) {
		this.webpagekey = webpagekey;
	}
	public Double getCustomPrice() {
		return customPrice;
	}
	public void setCustomPrice(Double customPrice) {
		this.customPrice = customPrice;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
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
	@Override
	public String toString() {
		return "CarModelInfoModel [processinstanceid=" + processinstanceid
				+ ", modelCode=" + modelCode + ", displacement=" + displacement
				+ ", approvedLoad=" + approvedLoad + ", rulePriceProvideType="
				+ rulePriceProvideType + ", institutionType=" + institutionType
				+ ", useProperty=" + useProperty + ", tonnage=" + tonnage
				+ ", wholeWeight=" + wholeWeight + ", customPrice="
				+ customPrice + ", webpagekey=" + webpagekey + "]";
	}

	
}
