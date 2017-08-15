package com.zzb.conf.controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DeptPayTypeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String check; 
	private String id;//paychannelid 
	private String paychannelname;
	private String paytypename;
	private String modifytime;
	private boolean state;
	private String deptid;
	private String agreementid;//agreeid 协议id 存协议表里的id
	private String providerid;
	private List<DeptPayTypeVo> selected;
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPaychannelname() {
		return paychannelname;
	}
	public void setPaychannelname(String paychannelname) {
		this.paychannelname = paychannelname;
	}
	public String getPaytypename() {
		return paytypename;
	}
	public void setPaytypename(String paytypename) {
		this.paytypename = paytypename;
	}
	public String getModifytime() {
		return modifytime;
	}
	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getProviderid() {
		return providerid;
	}
	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}
	public String getAgreementid() {
		return agreementid;
	}
	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}
	public List<DeptPayTypeVo> getSelected() {
		return selected;
	}
	public void setSelected(List<DeptPayTypeVo> selected) {
		this.selected = selected;
	}
	
}
