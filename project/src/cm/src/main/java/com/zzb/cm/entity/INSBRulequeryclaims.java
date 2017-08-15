package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRulequeryclaims extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 保险公司ID
	 */
	private String inscorpid;

	/**
	 * 保险公司代码
	 */
	private String inscorpcode;

	/**
	 * 保险公司名称
	 */
	private String inscorpname;

	/**
	 * 出险时间
	 */
	private String casestarttime;

	/**
	 * 结案时间
	 */
	private String caseendtime;

	/**
	 * 理赔金额
	 */
	private Double claimamount;

	/**
	 * 保单号
	 */
	private String policyid;

	/**
	 * 0 商业险 1 交强险
	 */
	private String risktype;

	private String policyno;

	public String getPolicyno() {
		return policyno;
	}

	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscorpid() {
		return inscorpid;
	}

	public void setInscorpid(String inscorpid) {
		this.inscorpid = inscorpid;
	}

	public String getInscorpcode() {
		return inscorpcode;
	}

	public void setInscorpcode(String inscorpcode) {
		this.inscorpcode = inscorpcode;
	}

	public String getInscorpname() {
		return inscorpname;
	}

	public void setInscorpname(String inscorpname) {
		this.inscorpname = inscorpname;
	}

	public String getCasestarttime() {
		return casestarttime;
	}

	public void setCasestarttime(String casestarttime) {
		this.casestarttime = casestarttime;
	}

	public String getCaseendtime() {
		return caseendtime;
	}

	public void setCaseendtime(String caseendtime) {
		this.caseendtime = caseendtime;
	}

	public Double getClaimamount() {
		return claimamount;
	}

	public void setClaimamount(Double claimamount) {
		this.claimamount = claimamount;
	}

	public String getPolicyid() {
		return policyid;
	}

	public void setPolicyid(String policyid) {
		this.policyid = policyid;
	}

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

}