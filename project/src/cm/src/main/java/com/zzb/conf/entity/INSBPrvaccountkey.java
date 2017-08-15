package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBPrvaccountkey extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 数据表id
	 */
	private String managerid;

	/**
	 * 参数名称
	 */
	private String paramname;

	/**
	 * 参数值
	 */
	private String paramvalue;
	
	/**
	 * 所属机构id
	 */
 	private String deptid;
 	
	
	/**
	 * 所属机构
	 */
 	private String deptname;


	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getParamvalue() {
		return paramvalue;
	}

	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	@Override
	public String toString() {
		return "INSBPrvaccountkey [managerid=" + managerid + ", paramname="
				+ paramname + ", paramvalue=" + paramvalue + "]";
	}

}