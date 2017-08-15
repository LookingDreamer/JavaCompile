package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgreementdept extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 协议ID
	 */
	private String agreementid;
 
	/**
	 * 集团
	 */
	private String deptid1;

	/**
	 * 平台公司
	 */
	private String deptid2;

	/**
	 * 法人公司
	 */
	private String deptid3;

	/**
	 * 分公司
	 */
	private String deptid4;

	/**
	 * 网点
	 */
	private String deptid5;

	/**
	 * 承保接口
	 */
	private String underwrite;

	/**
	 * 优先级
	 */
	private String scaleflag;

	/**
	 * 设置权限
	 */
	private String permflag;
	private String providerid;

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

	public String getUnderwrite() {
		return underwrite;
	}

	public void setUnderwrite(String underwrite) {
		this.underwrite = underwrite;
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

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

}