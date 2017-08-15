package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRiskkind extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String riskid;

	/**
	 * 
	 */
	private String kindcode;

	/**
	 * 
	 */
	private String kindname;

	/**
	 * 
	 */
	private String kindtype;

	/**
	 * 
	 */
	private String isamount;

	/**
	 * 
	 */
	private String amountselect;

	/**
	 * 
	 */
	private String notdeductible;

	/**
	 * 
	 */
	private String preriskkind;

	/**
	 * 
	 */
	private String isusing;

	public String getRiskid() {
		return riskid;
	}

	public void setRiskid(String riskid) {
		this.riskid = riskid;
	}

	public String getKindcode() {
		return kindcode;
	}

	public void setKindcode(String kindcode) {
		this.kindcode = kindcode;
	}

	public String getKindname() {
		return kindname;
	}

	public void setKindname(String kindname) {
		this.kindname = kindname;
	}

	public String getKindtype() {
		return kindtype;
	}

	public void setKindtype(String kindtype) {
		this.kindtype = kindtype;
	}

	public String getIsamount() {
		return isamount;
	}

	public void setIsamount(String isamount) {
		this.isamount = isamount;
	}

	public String getAmountselect() {
		return amountselect;
	}

	public void setAmountselect(String amountselect) {
		this.amountselect = amountselect;
	}

	public String getIsusing() {
		return isusing;
	}

	public void setIsusing(String isusing) {
		this.isusing = isusing;
	}

	public String getNotdeductible() {
		return notdeductible;
	}

	public void setNotdeductible(String notdeductible) {
		this.notdeductible = notdeductible;
	}

	public String getPreriskkind() {
		return preriskkind;
	}

	public void setPreriskkind(String preriskkind) {
		this.preriskkind = preriskkind;
	}

	@Override
	public String toString() {
		return "INSBRiskkind [riskid=" + riskid + ", kindcode=" + kindcode
				+ ", kindname=" + kindname + ", kindtype=" + kindtype
				+ ", isamount=" + isamount + ", amountselect=" + amountselect
				+ ", notdeductible=" + notdeductible + ", preriskkind="
				+ preriskkind + ", isusing=" + isusing + "]";
	}


}