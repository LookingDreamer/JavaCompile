package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgreementscope extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String agreementid;

	/**
	 * 1 -覆盖区域
            2 -覆盖网点
	 */
	private String scopetype;

	/**
	 * 
	 */
	private String province;

	/**
	 * 
	 */
	private String city;

	/**
	 * 
	 */
	private String county;

	/**
	 * 
	 */
	private String deptid;

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getScopetype() {
		return scopetype;
	}

	public void setScopetype(String scopetype) {
		this.scopetype = scopetype;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

}