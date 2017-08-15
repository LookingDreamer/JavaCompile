package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("insureAddress")
public class InsureAddress {

	/**
	 * 省
	 */
	@XStreamAlias("provinceName")
	private String provinceName;
	/**
	 * 省代码
	 */
	@XStreamAlias("provinceCode")
	private String provinceCode;
	/**
	 * 市
	 */
	@XStreamAlias("cityName")
	private String cityName;
	/**
	 * 市代码
	 */
	@XStreamAlias("cityCode")
	private String cityCode;
	
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
	
}
