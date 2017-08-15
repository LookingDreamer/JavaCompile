package com.zzb.cm.Interface.entity.cif.model;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class Beneficiary extends BaseEntity implements Identifiable {
	String personName; // '姓名'
	String gender;// '性别'
	Date birthday;// '生日'
	String certNumber;// '证件号码'
	String certType;// '证件类型'
	String age;// '年龄'
	String scaned;// '身份证是否通过扫描得到'
	String address;
	String policyno;
	String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPolicyno() {
		return policyno;
	}

	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getScaned() {
		return scaned;
	}

	public void setScaned(String scaned) {
		this.scaned = scaned;
	}

}
