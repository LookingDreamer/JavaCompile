package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBInterface extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 接口名称
	 */
	private String name;

	/**
	 * 接口地址
	 */
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}