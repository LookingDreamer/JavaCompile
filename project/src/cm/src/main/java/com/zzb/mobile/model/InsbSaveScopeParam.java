package com.zzb.mobile.model;

import java.util.List;

public class InsbSaveScopeParam {
	/**
	 * 城市列表
	 */
	private List<String> citys;
	/**
	 * 协议id
	 */
	private String  agreementid;
	/**
	 * 省区code
	 */
	private String  province;


	public List<String> getCitys() {
		return citys;
	}

	public void setCitys(List<String> citys) {
		this.citys = citys;
	}

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
