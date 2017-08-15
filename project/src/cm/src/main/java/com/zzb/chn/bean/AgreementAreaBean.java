package com.zzb.chn.bean;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AgreementAreaBean {
	private String province; //省代码
	private String provinceName; //省名称
	private List<CityBean> citys;
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public List<CityBean> getCitys() {
		return citys;
	}
	public void setCitys(List<CityBean> citys) {
		this.citys = citys;
	}

}