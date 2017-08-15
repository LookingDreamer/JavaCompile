package com.zzb.mobile.model;


/**
 * 车辆信息
 * 
 * @author qiu
 *
 */
public class AppCarInfo {
	/**
	 * 品牌型号
	 */
	private String modelCode;
	/**
	 * 发动机号
	 */
	private String engineNo;
	 
    /**
     * 车架号
     */
    private String vin;
    /**
     *  是否为过户车     1 True 0 false
     */
    private boolean istransfer;
    /**
     * 过户日期
     */
    private String chgOwnerDate;
    /**
     * 初始登记日期
     */
    private String firstRegisterDate;
    /**
     * 所属性质
     */
    private String property;
    /**
     * 车辆性质
     */
    private String carproperty;
    
	public boolean isIstransfer() {
		return istransfer;
	}
	public void setIstransfer(boolean istransfer) {
		this.istransfer = istransfer;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getCarproperty() {
		return carproperty;
	}
	public void setCarproperty(String carproperty) {
		this.carproperty = carproperty;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
    
}
