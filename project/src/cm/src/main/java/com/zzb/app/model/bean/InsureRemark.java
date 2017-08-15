package com.zzb.app.model.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("remark")
public class InsureRemark {
	/**
	 * 备注信息
	 */
	@XStreamAlias("code")
	private String code;
	/**
	 * 代码普通备注,不传默认设定
	 */
	@XStreamAlias("value")
	private String value;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
