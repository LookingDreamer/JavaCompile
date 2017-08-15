package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRiskitem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String riskid;

	/**
	 * 
	 */
	private String itemname;

	/**
	 * 
	 */
	private String itemcode;

	/**
	 * 
	 */
	private String itemtype;

	/**
	 * 
	 */
	private String isusing;

	/**
	 * 
	 */
	private String isquotemust;

	/**
	 * 
	 */
	private String inputtype;

	/**
	 * 
	 */
	private String optional;

	/**
	 * 
	 */
	private String isunderwriteusing;

	/**
	 * 
	 */
	private String isunderwritemust;

	public String getRiskid() {
		return riskid;
	}

	public void setRiskid(String riskid) {
		this.riskid = riskid;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getIsusing() {
		return isusing;
	}

	public void setIsusing(String isusing) {
		this.isusing = isusing;
	}

	public String getIsquotemust() {
		return isquotemust;
	}

	public void setIsquotemust(String isquotemust) {
		this.isquotemust = isquotemust;
	}

	public String getInputtype() {
		return inputtype;
	}

	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String getIsunderwriteusing() {
		return isunderwriteusing;
	}

	public void setIsunderwriteusing(String isunderwriteusing) {
		this.isunderwriteusing = isunderwriteusing;
	}

	public String getIsunderwritemust() {
		return isunderwritemust;
	}

	public void setIsunderwritemust(String isunderwritemust) {
		this.isunderwritemust = isunderwritemust;
	}

}