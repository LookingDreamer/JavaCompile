package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBPersonVO extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/*
	 * 驾驶人人员id
	 */
	private String id;
	/*
	 * 指定驾驶人中间表id
	 */
	private String specifyDriverId;
	/*
	 * 驾驶人姓名
	 */
	private String name;
	/*
	 * 出生日期
	 */
	private String birthday;
	/*
	 * 性别
	 */
	private String gender;
	/*
	 * 驾驶证类型
	 */
	private String licensetype;
	/*
	 * 驾驶证号
	 */
	private String licenseno;
	/*
	 * 驾驶证发照日期
	 */
	private String licensedate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpecifyDriverId() {
		return specifyDriverId;
	}
	public void setSpecifyDriverId(String specifyDriverId) {
		this.specifyDriverId = specifyDriverId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLicensetype() {
		return licensetype;
	}
	public void setLicensetype(String licensetype) {
		this.licensetype = licensetype;
	}
	public String getLicenseno() {
		return licenseno;
	}
	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}
	public String getLicensedate() {
		return licensedate;
	}
	public void setLicensedate(String licensedate) {
		this.licensedate = licensedate;
	}
	public INSBPersonVO() {
		super();
	}
	
}
