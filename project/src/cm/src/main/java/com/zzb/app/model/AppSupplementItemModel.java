package com.zzb.app.model;

public class AppSupplementItemModel {
	private String enName;// 英文名称
	private String cnName;// 中文名称
	private String type;// 类型
	private String valScope;// 取值范围
	private String defaultVal;// 默认值

	public String getEnName() {
		return enName;
	}

	public String getCnName() {
		return cnName;
	}

	public String getType() {
		return type;
	}

	public String getValScope() {
		return valScope;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValScope(String valScope) {
		this.valScope = valScope;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	@Override
	public String toString() {
		return "AppSupplementItemModel [enName=" + enName + ", cnName="
				+ cnName + ", type=" + type + ", valScope=" + valScope
				+ ", defaultVal=" + defaultVal + "]";
	}

}
