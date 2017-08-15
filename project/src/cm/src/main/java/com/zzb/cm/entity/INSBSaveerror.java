package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBSaveerror extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 报文
	 */
	private String message;

	/**
	 * 错误描述
	 */
	private String errordesc;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrordesc() {
		return errordesc;
	}

	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}

}