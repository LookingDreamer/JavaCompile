package com.zzb.conf.controller.vo;

import java.io.Serializable;
import java.util.List;

public class InterfaceVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String agreementinterfaceid;
	private String check;
	private String checkflag;
	private String interfaceid;
	private String interfacename;
	private String isfree;
	private String isfreename;
	private String monthfree;
	private String perfee;
	private String channelinnercode;
	private String extendspattern ;
	private List<InterfaceVo> interfaces;
	private String pv1;
	private String pv2;
	private String pv3;
	private String pv4;
	public String getAgreementinterfaceid() {
		return agreementinterfaceid;
	}
	public void setAgreementinterfaceid(String agreementinterfaceid) {
		this.agreementinterfaceid = agreementinterfaceid;
	}
	public String getInterfaceid() {
		return interfaceid;
	}
	public void setInterfaceid(String interfaceid) {
		this.interfaceid = interfaceid;
	}
	public String getCheckflag() {
		return checkflag;
	}
	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getInterfacename() {
		return interfacename;
	}
	public void setInterfacename(String interfacename) {
		this.interfacename = interfacename;
	}
	public String getIsfree() {
		return isfree;
	}
	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}
	public String getIsfreename() {
		return isfreename;
	}
	public void setIsfreename(String isfreename) {
		this.isfreename = isfreename;
	}
	public String getMonthfree() {
		return monthfree;
	}
	public void setMonthfree(String monthfree) {
		this.monthfree = monthfree;
	}
	public String getPerfee() {
		return perfee;
	}
	public void setPerfee(String perfee) {
		this.perfee = perfee;
	}
	public String getChannelinnercode() {
		return channelinnercode;
	}
	public void setChannelinnercode(String channelinnercode) {
		this.channelinnercode = channelinnercode;
	}
	public List<InterfaceVo> getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(List<InterfaceVo> interfaces) {
		this.interfaces = interfaces;
	}

	public String getExtendspattern() {
		return extendspattern;
	}

	public void setExtendspattern(String extendspattern) {
		this.extendspattern = extendspattern;
	}

	public String getPv1() {
		return pv1;
	}

	public void setPv1(String pv1) {
		this.pv1 = pv1;
	}

	public String getPv2() {
		return pv2;
	}

	public void setPv2(String pv2) {
		this.pv2 = pv2;
	}

	public String getPv3() {
		return pv3;
	}

	public void setPv3(String pv3) {
		this.pv3 = pv3;
	}

	public String getPv4() {
		return pv4;
	}

	public void setPv4(String pv4) {
		this.pv4 = pv4;
	}
}
