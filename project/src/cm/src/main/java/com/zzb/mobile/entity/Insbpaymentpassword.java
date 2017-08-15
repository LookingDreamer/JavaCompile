package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class Insbpaymentpassword extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	private String jobNum;
	
	private String phone;
	
	private String password;

	public String getJobNum() {
		return jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Insbpaymentpassword [jobNum=" + jobNum + ", phone=" + phone
				+ ", password=" + password + "]";
	}
	

	

	
	
	
	
	
	
}
