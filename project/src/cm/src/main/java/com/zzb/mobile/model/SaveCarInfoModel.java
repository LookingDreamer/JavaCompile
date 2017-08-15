package com.zzb.mobile.model;

public class SaveCarInfoModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
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
	 * 平台查询传过来的车辆品牌
	 */
	private String cifstandardname;
	/**
	 * vin
	 * 不隐藏
	 */
	private String tempVin;
	/**
	 * 发动机号  不隐藏
	 */
	private String tempEngineNo;
	
	public String getTempVin() {
		return tempVin;
	}

	public void setTempVin(String tempVin) {
		this.tempVin = tempVin;
	}

	public String getTempEngineNo() {
		return tempEngineNo;
	}

	public void setTempEngineNo(String tempEngineNo) {
		this.tempEngineNo = tempEngineNo;
	}

	public String getCifstandardname() {
		return cifstandardname;
	}

	public void setCifstandardname(String cifstandardname) {
		this.cifstandardname = cifstandardname;
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
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getDrivinglicense() {
		return drivinglicense;
	}
	public void setDrivinglicense(String drivinglicense) {
		this.drivinglicense = drivinglicense;
	}
	public String getWebpagekey() {
		return webpagekey;
	}
	public void setWebpagekey(String webpagekey) {
		this.webpagekey = webpagekey;
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

	@Override
	public String toString() {
		return "SaveCarInfoModel [processinstanceid=" + processinstanceid
				+ ", modelCode=" + modelCode + ", vin=" + vin + ", engineNo="
				+ engineNo + ", chgOwnerFlag=" + chgOwnerFlag
				+ ", chgOwnerDate=" + chgOwnerDate + ", firstRegisterDate="
				+ firstRegisterDate + ", webpagekey=" + webpagekey
				+ ", drivinglicense=" + drivinglicense + ", fileid=" + fileid
				+ ", institutionType=" + institutionType + ", useProperty="
				+ useProperty + ", cifstandardname=" + cifstandardname
				+ ", tempVin=" + tempVin + ", tempEngineNo=" + tempEngineNo
				+ "]";
	}

	
}
