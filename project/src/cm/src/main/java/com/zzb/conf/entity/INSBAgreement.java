package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAgreement extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/**
	 * 行号
	 */
	private String rownum;

	/**
	 * 
	 */
	private String agreementcode;

	/**
	 * 
	 */
	private String agreementname;

	/**
	 * 
	 */
	private String agreementstatus;

	/**
	 * 
	 */
	private String providerid;

	/**
	 * 任务规则
	 */
	private String agreementdrule;
	/**
	 * 传统规则
	 */
	private String agreementtrule;

	/**
	 * 
	 */
	private String deptid;
	private String agreementrule;

    /**
     * 是否开启快速续保
     */
    private Integer renewalenable;

	private Integer underwritestatus;

	/**
	 * 协议类型：内部协议-0、外部协议-1
	 */
	private Integer agreementType;

	public Integer getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(Integer agreementType) {
		this.agreementType = agreementType;
	}

	public Integer getUnderwritestatus() {
		return underwritestatus;
	}

	public void setUnderwritestatus(Integer underwritestatus) {
		this.underwritestatus = underwritestatus;
	}

	public String getAgreementcode() {
		return agreementcode;
	}

	public void setAgreementcode(String agreementcode) {
		this.agreementcode = agreementcode;
	}

	public String getAgreementname() {
		return agreementname;
	}

	public void setAgreementname(String agreementname) {
		this.agreementname = agreementname;
	}

	public String getAgreementstatus() {
		return agreementstatus;
	}

	public void setAgreementstatus(String agreementstatus) {
		this.agreementstatus = agreementstatus;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}


	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getAgreementdrule() {
		return agreementdrule;
	}

	public void setAgreementdrule(String agreementdrule) {
		this.agreementdrule = agreementdrule;
	}

	public String getAgreementtrule() {
		return agreementtrule;
	}

	public void setAgreementtrule(String agreementtrule) {
		this.agreementtrule = agreementtrule;
	}
	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	/*@Override
	public String toString() {
		return "INSBAgreement [agreementcode=" + agreementcode
				+ ", agreementname=" + agreementname + ", agreementstatus="
				+ agreementstatus + ", providerid=" + providerid
				+ ", agreementdrule=" + agreementdrule + ", agreementtrule="
				+ agreementtrule + ", deptid=" + deptid + ",rownum="+rownum+"]";
	}*/

	@Override
	public String toString() {
		return "INSBAgreement [rownum=" + rownum + ", agreementcode="
				+ agreementcode + ", agreementname=" + agreementname
				+ ", agreementstatus=" + agreementstatus + ", providerid="
				+ providerid + ", agreementdrule=" + agreementdrule
				+ ", agreementtrule=" + agreementtrule + ", deptid=" + deptid
				+ ", agreementrule=" + agreementrule + ", agreementType=" + agreementType + "]";
	}
	
	public String getAgreementrule() {
		return agreementrule;
	}

	public void setAgreementrule(String agreementrule) {
		this.agreementrule = agreementrule;
	}

    public Integer getRenewalenable() {
        return renewalenable;
    }

    public void setRenewalenable(Integer renewalenable) {
        this.renewalenable = renewalenable;
    }
}