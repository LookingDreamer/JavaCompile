package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgentstandbyinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 代理人id
	 */
	private String agentid;

	/**
	 * 快刷类型
	 */
	private String QuickBrushType;

	/**
	 * 备用字段
	 */
	private String isacceptmessage;


	/**
	 * 备用字段
	 */
	private String ext2;

	/**
	 * 备用字段
	 */
	private String ext3;

	/**
	 * 备用字段
	 */
	private String ext4;

	/**
	 * 备用字段
	 */
	private String ext5;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getQuickBrushType() {
		return QuickBrushType;
	}

	public void setQuickBrushType(String QuickBrushType) {
		this.QuickBrushType = QuickBrushType;
	}

	public String getIsacceptmessage() {
		return isacceptmessage;
	}

	public void setIsacceptmessage(String isacceptmessage) {
		this.isacceptmessage = isacceptmessage;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

}