package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBEdidescription extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * EDIID
	 */
	private String ediid;

	/**
	 * 能力描述名称
	 */
	private String name;

	/**
	 * 路径ip
	 */
	private String ippath;

	

	public String getEdiid() {
		return ediid;
	}

	public void setEdiid(String ediid) {
		this.ediid = ediid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIppath() {
		return ippath;
	}

	public void setIppath(String ippath) {
		this.ippath = ippath;
	}
}