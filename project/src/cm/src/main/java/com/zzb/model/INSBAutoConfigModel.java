package com.zzb.model;

import java.util.List;

import com.zzb.conf.entity.INSBAutoconfig;


public class INSBAutoConfigModel {
	
	/**
	 * 协议id
	 */
	private String agreementid;
	
	/**
	 * 自动化配置表(续保)
	 */
	private List<INSBAutoconfig> insbAutoConfiglistxb;
	
	/**
	 * 自动化配置表(核保)
	 */
	private List<INSBAutoconfig> insbAutoConfiglisthb;
	
	/**
	 * 自动化配置表(承保)
	 */
	private List<INSBAutoconfig> insbAutoConfiglistcb;
	/**
	 * 自动化配置表(报价方式)
	 */
	private List<INSBAutoconfig> insbAutoConfiglistbjfs;


	public List<INSBAutoconfig> getInsbAutoConfiglistxb() {
		return insbAutoConfiglistxb;
	}

	public void setInsbAutoConfiglistxb(List<INSBAutoconfig> insbAutoConfiglistxb) {
		this.insbAutoConfiglistxb = insbAutoConfiglistxb;
	}

	public List<INSBAutoconfig> getInsbAutoConfiglisthb() {
		return insbAutoConfiglisthb;
	}

	public void setInsbAutoConfiglisthb(List<INSBAutoconfig> insbAutoConfiglisthb) {
		this.insbAutoConfiglisthb = insbAutoConfiglisthb;
	}

	public List<INSBAutoconfig> getInsbAutoConfiglistbjfs() {
		return insbAutoConfiglistbjfs;
	}

	public void setInsbAutoConfiglistbjfs(
			List<INSBAutoconfig> insbAutoConfiglistbjfs) {
		this.insbAutoConfiglistbjfs = insbAutoConfiglistbjfs;
	}

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public List<INSBAutoconfig> getInsbAutoConfiglistcb() {
		return insbAutoConfiglistcb;
	}

	public void setInsbAutoConfiglistcb(List<INSBAutoconfig> insbAutoConfiglistcb) {
		this.insbAutoConfiglistcb = insbAutoConfiglistcb;
	}
	
}
