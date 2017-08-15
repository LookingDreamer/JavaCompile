package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSCDeptpermissionset extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 机构编码
	 */
	private String comcode;

	/**
	 * 试用用户默认权限包id
	 */
	private String trysetid;

	/**
	 * 正式用户默认权限包id
	 */
	private String formalsetid;

	/**
	 * 渠道用户默认权限包id
	 */
	private String channelsetid;

	public String getComcode() {
		return comcode;
	}

	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getTrysetid() {
		return trysetid;
	}

	public void setTrysetid(String trysetid) {
		this.trysetid = trysetid;
	}

	public String getFormalsetid() {
		return formalsetid;
	}

	public void setFormalsetid(String formalsetid) {
		this.formalsetid = formalsetid;
	}

	public String getChannelsetid() {
		return channelsetid;
	}

	public void setChannelsetid(String channelsetid) {
		this.channelsetid = channelsetid;
	}

}