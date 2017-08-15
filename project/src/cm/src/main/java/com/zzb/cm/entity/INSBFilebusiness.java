package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBFilebusiness extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件库id
	 */
	private String filelibraryid;

	/**
	 * 分类
	 */
	private String type;

	/**
	 * 值
	 */
	private String code;

	public String getFilelibraryid() {
		return filelibraryid;
	}

	public void setFilelibraryid(String filelibraryid) {
		this.filelibraryid = filelibraryid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}