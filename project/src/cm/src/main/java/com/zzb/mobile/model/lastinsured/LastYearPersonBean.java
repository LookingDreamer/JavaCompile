package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;

public class LastYearPersonBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;//车主姓名
	String idtype;//车主证件类型
	String idno;//证件号
	String mobile;//联系电话
	String email;//email
	String addrss;//地址
	
	public String getAddrss() {
		return addrss;
	}
	public void setAddrss(String addrss) {
		this.addrss = addrss==null?null:addrss.trim();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name==null?null:name.trim();
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype==null?null:idtype.trim();
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno==null?null:idno.trim();
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile==null?null:mobile.trim();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email==null?null:email.trim();
	}
	
}
