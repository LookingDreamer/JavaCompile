package com.zzb.mobile.model;

import java.util.List;
import java.util.Map;

public class VerificationConfigBean {

	/**
	 * 险别编码
	 */
	private String kindcode;
	/**
	 * 供应商id
	 */
	private String provideid;
	/**
	 * 险别中文名称
	 */
	private String kindname;
	/**
	 * 不支持的报价公司列表
	 */
	private List<Map<String, String>> providers;
	
	public String getKindcode() {
		return kindcode;
	}
	public void setKindcode(String kindcode) {
		this.kindcode = kindcode;
	}
	public String getKindname() {
		return kindname;
	}
	public void setKindname(String kindname) {
		this.kindname = kindname;
	}
	public List<Map<String, String>> getProviders() {
		return providers;
	}
	public void setProviders(List<Map<String, String>> providers) {
		this.providers = providers;
	}
	public String getProvideid() {
		return provideid;
	}
	public void setProvideid(String provideid) {
		this.provideid = provideid;
	}
	
}
