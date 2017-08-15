package com.zzb.mobile.model;

import java.util.List;

public class InsbSaveTruleParms {
	/**
	 * 城市列表
	 */
	private List<String> citys;
	/**
	 * 协议id
	 */
	private String  trulename;
	public List<String> getCitys() {
		return citys;
	}
	public void setCitys(List<String> citys) {
		this.citys = citys;
	}
	public String getTrulename() {
		return trulename;
	}
	public void setTrulename(String trulename) {
		this.trulename = trulename;
	}
}
