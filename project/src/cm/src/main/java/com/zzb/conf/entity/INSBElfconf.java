package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBElfconf extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 行号
	 */
	private String rownum;
	/**
	 * 精灵名称
	 */
	private String elfname;

	/**
	 * 存放路径
	 */
	private String elfpath;
	/**
	 * 保险公司id
	 */
	private String proid;
	
	/**
	 * 精灵类型1：半自动2全自动统一3全自动非统一
	 */
	private String elftype;
	
	/**
	 * 能力配置
	 */
	private String capacityconf;
	
	public String getElfname() {
		return elfname;
	}

	public void setElfname(String elfname) {
		this.elfname = elfname;
	}

	public String getElfpath() {
		return elfpath;
	}

	public void setElfpath(String elfpath) {
		this.elfpath = elfpath;
	}

	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	public String getCapacityconf() {
		return capacityconf;
	}

	public void setCapacityconf(String capacityconf) {
		this.capacityconf = capacityconf;
	}

	public String getElftype() {
		return elftype;
	}

	public void setElftype(String elftype) {
		this.elftype = elftype;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	@Override
	public String toString() {
		return "INSBElfconf [rownum=" + rownum + ", elfname=" + elfname
				+ ", elfpath=" + elfpath + ", proid=" + proid + ", elftype="
				+ elftype + ", capacityconf=" + capacityconf + "]";
	}
	
}