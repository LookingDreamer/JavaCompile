package com.zzb.mobile.model;

public class InsuredQuoteCreateModel {

	/**
	 * 代理人id
	 */
	private String agentid;
	/**
	 * 车牌号
	 */
	private String plateNumber;
	/**
	 * 车主姓名
	 */
	private String owerName;
	/**
	 * 省名称
	 */
	private String provinceName;
	/**
	 * 省编码
	 */
	private String provinceCode;
	/**
	 * 市名称
	 */
	private String cityName;
	/**
	 * 市编码
	 */
	private String cityCode;
	/**
	 * 是否快速续保 0 不是  1 是
	 */
	private String flag;
	/**
	 * 业管id  cm后台代客录单
	 */
	private String userid;
	/**
	 * 懒掌柜id
	 */
	private String requirementid;
	/**
	 * 购买人id，通过懒掌柜购买
	 */
	private String purchaserid;
	/**
	 * 懒掌柜分享人id
	 */
	private String lzgshareid;
	/**
	 * 行驶证扫描地址
	 */
	private String address;
	/**
	 * 车主身份证
	 */
	private String personIdno;
	/**
	 * 业务来源 IOS版   Android版   网页版  微信版     渠道对接    其他
	 */
	private String datasourcesfrom;

	/**
	 * 渠道用户uuid(minizzb)
	 * @return
	 */
	private String channeluserid;

	public String getChanneluserid() {
		return channeluserid;
	}

	public void setChanneluserid(String channeluserid) {
		this.channeluserid = channeluserid;
	}

	public String getDatasourcesfrom() {
		return datasourcesfrom;
	}
	public void setDatasourcesfrom(String datasourcesfrom) {
		this.datasourcesfrom = datasourcesfrom;
	}
	public String getPersonIdno() {
		return personIdno;
	}
	public void setPersonIdno(String personIdno) {
		this.personIdno = personIdno;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getOwerName() {
		return owerName;
	}
	public void setOwerName(String owerName) {
		this.owerName = owerName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getRequirementid() {
		return requirementid;
	}
	public void setRequirementid(String requirementid) {
		this.requirementid = requirementid;
	}
	public String getPurchaserid() {
		return purchaserid;
	}
	public void setPurchaserid(String purchaserid) {
		this.purchaserid = purchaserid;
	}
	public String getLzgshareid() {
		return lzgshareid;
	}
	public void setLzgshareid(String lzgshareid) {
		this.lzgshareid = lzgshareid;
	}
	
}
