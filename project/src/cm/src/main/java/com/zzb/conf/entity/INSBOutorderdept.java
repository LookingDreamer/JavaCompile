package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBOutorderdept extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
 
	/**
	 * 
	 */
	private String agreementid;

	/**
	 * 
	 */
	private String deptid1;

	/**
	 * 
	 */
	private String deptid2;

	/**
	 * 
	 */
	private String deptid3;

	/**
	 * 
	 */
	private String deptid4;

	/**
	 * 
	 */
	private String deptid5;

	/**
	 * 
	 */
	private String scaleflag;

	/**
	 * 
	 */
	private String permflag;

	/**
	 * 
	 */
	private String underwritinginter;

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getDeptid1() {
		return deptid1;
	}

	public void setDeptid1(String deptid1) {
		this.deptid1 = deptid1;
	}

	public String getDeptid2() {
		return deptid2;
	}

	public void setDeptid2(String deptid2) {
		this.deptid2 = deptid2;
	}

	public String getDeptid3() {
		return deptid3;
	}

	public void setDeptid3(String deptid3) {
		this.deptid3 = deptid3;
	}

	public String getDeptid4() {
		return deptid4;
	}

	public void setDeptid4(String deptid4) {
		this.deptid4 = deptid4;
	}

	public String getDeptid5() {
		return deptid5;
	}

	public void setDeptid5(String deptid5) {
		this.deptid5 = deptid5;
	}

	public String getScaleflag() {
		return scaleflag;
	}

	public void setScaleflag(String scaleflag) {
		this.scaleflag = scaleflag;
	}

	public String getPermflag() {
		return permflag;
	}

	public void setPermflag(String permflag) {
		this.permflag = permflag;
	}

	public String getUnderwritinginter() {
		return underwritinginter;
	}

	public void setUnderwritinginter(String underwritinginter) {
		this.underwritinginter = underwritinginter;
	}

	@Override
	public String toString() {
		return "INSBOutorderdept [agreementid=" + agreementid + ", deptid1="
				+ deptid1 + ", deptid2=" + deptid2 + ", deptid3=" + deptid3
				+ ", deptid4=" + deptid4 + ", deptid5=" + deptid5
				+ ", scaleflag=" + scaleflag + ", permflag=" + permflag
				+ ", underwritinginter=" + underwritinginter + "]";
	}

	
	
	
}