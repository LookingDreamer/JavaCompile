package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBProducediscount extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商产品优惠名称
	 */
	private String name;

	/**
	 * 说明
	 */
	private String note;

	/**
	 * 类型
	 */
	private String  type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String  getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}