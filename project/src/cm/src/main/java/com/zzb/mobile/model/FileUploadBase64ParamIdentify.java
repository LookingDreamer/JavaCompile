package com.zzb.mobile.model;

public class FileUploadBase64ParamIdentify extends FileUploadBase64Param{
	private String name; //身份证真实姓名
	private String idno;
	private String idnotype;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getIdnotype() {
		return idnotype;
	}

	public void setIdnotype(String idnotype) {
		this.idnotype = idnotype;
	}
}
