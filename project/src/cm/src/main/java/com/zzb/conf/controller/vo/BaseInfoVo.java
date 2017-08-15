package com.zzb.conf.controller.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BaseInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String agreementid; 
	private String deptid;
	private String channelid;
	private String agreementstatus;
	private String province;
	private List<String> citys;
	public String getAgreementid() {
		return agreementid;
	}
	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}
	public String getAgreementstatus() {
		return agreementstatus;
	}
	public void setAgreementstatus(String agreementstatus) {
		this.agreementstatus = agreementstatus;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public List<String> getCitys() {
		return citys;
	}
	public void setCitys(List<String> citys) {
		this.citys = citys;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	
}
