package com.zzb.mobile.model;

public class CallPlatformQueryModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 代理人id
	 */
	private String agentid;
	/**
	 * 车牌号
	 */
	private String plateNumber;
	/**
	 * 省编码
	 */
	private String provinceCode;
	/**
	 * 市编码
	 */
	private String cityCode;
	/**
	 * 是否快速续保 0 不是  1 是
	 */
	private String flag;
	/**
	 * 代理人姓名
	 */
	private String agentName;
	/**
	 * 快速续保选的上年投保公司id
	 */
	private String provid;
	/**
	 * 我要比价，只获取一个车型  0 不是比价 1比价 需要获唯一车型
	 */
	private String parityflag;
	/***
	 * 车主姓名
	 */
	private String owerName;
	/**
	 * 车主身份证
	 */
	private String personIdno;
	/**
	 * 车架号
	 */
	private String vincode;

	private String queryFlag;

	public String getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(String queryFlag) {
		this.queryFlag = queryFlag;
	}

	public String getVincode() {
		return vincode;
	}
	public void setVincode(String vincode) {
		this.vincode = vincode;
	}
	public String getPersonIdno() {
		return personIdno;
	}
	public void setPersonIdno(String personIdno) {
		this.personIdno = personIdno;
	}
	public String getOwerName() {
		return owerName;
	}
	public void setOwerName(String owerName) {
		this.owerName = owerName;
	}
	public String getParityflag() {
		return parityflag;
	}
	public void setParityflag(String parityflag) {
		this.parityflag = parityflag;
	}
	public String getProvid() {
		return provid;
	}
	public void setProvid(String provid) {
		this.provid = provid;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
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
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
