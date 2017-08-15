package com.zzb.model;

public class AppCodeModel {

	/**
	 * 表现值
	 */
	private String codeName;
	
	/**
	 * 真实值
	 */
	private String codeValue;
	
	/**
	 * 数据类型
	 */
	private String codeType;
	

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	@Override
	public String toString() {
		return "AppCodeModel [codeName=" + codeName + ", codeValue="
				+ codeValue + ", codeType=" + codeType + "]";
	}

	
	
	
	
	
}
