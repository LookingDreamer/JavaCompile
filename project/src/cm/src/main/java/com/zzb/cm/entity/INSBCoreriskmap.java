package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCoreriskmap extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 保险公司名称
	 */
	private String inscomname;

	/**
	 * 险种代码
	 */
	private String riskcode;

	/**
	 * 险种名称
	 */
	private String riskname;

	/**
	 * 商业险险别代码
	 */
	private String riskkindcode;

	/**
	 * 险别名称
	 */
	private String riskkindname;

	/**
	 * 车辆类型(0-机动车，1-摩托车，2-拖拉机）
	 */
	private String cmcartype;

	/**
	 * 掌中保险种代码
	 */
	private String cmriskcode;

	/**
	 * 掌中保险种名称
	 */
	private String cmriskname;

	/**
	 * 掌中保商业险险别代码
	 */
	private String cmriskkindcode;

	/**
	 * 掌中保险别名称
	 */
	private String cmriskkindname;

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getInscomname() {
		return inscomname;
	}

	public void setInscomname(String inscomname) {
		this.inscomname = inscomname;
	}

	public String getRiskcode() {
		return riskcode;
	}

	public void setRiskcode(String riskcode) {
		this.riskcode = riskcode;
	}

	public String getRiskname() {
		return riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public String getRiskkindcode() {
		return riskkindcode;
	}

	public void setRiskkindcode(String riskkindcode) {
		this.riskkindcode = riskkindcode;
	}

	public String getRiskkindname() {
		return riskkindname;
	}

	public void setRiskkindname(String riskkindname) {
		this.riskkindname = riskkindname;
	}

	public String getCmcartype() {
		return cmcartype;
	}

	public void setCmcartype(String cmcartype) {
		this.cmcartype = cmcartype;
	}

	public String getCmriskcode() {
		return cmriskcode;
	}

	public void setCmriskcode(String cmriskcode) {
		this.cmriskcode = cmriskcode;
	}

	public String getCmriskname() {
		return cmriskname;
	}

	public void setCmriskname(String cmriskname) {
		this.cmriskname = cmriskname;
	}

	public String getCmriskkindcode() {
		return cmriskkindcode;
	}

	public void setCmriskkindcode(String cmriskkindcode) {
		this.cmriskkindcode = cmriskkindcode;
	}

	public String getCmriskkindname() {
		return cmriskkindname;
	}

	public void setCmriskkindname(String cmriskkindname) {
		this.cmriskkindname = cmriskkindname;
	}

}