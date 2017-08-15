package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class InsRecordNumberVO extends BaseEntity implements Identifiable {

	private static final long serialVersionUID = 1L;
	/**
	 * 任务id
	 */
	private String taskid;
	
	/**
	 * 商业险投保单号
	 */
	private String businessPolicyNum;

	/**
	 * 交强险投保单号
	 */
	private String strongPolicyNum;

	/**
	 * 支付号
	 */
	private String payNum;

	/**
	 * 校验码
	 */
	private String checkCode;
	
	/**
	 * 是否已在保险公司核保通过
	 */
	private String underwrited;
	
	/**
	 * 保险公司id
	 */
	private String inscomcode;

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getBusinessPolicyNum() {
		return businessPolicyNum;
	}

	public void setBusinessPolicyNum(String businessPolicyNum) {
		this.businessPolicyNum = businessPolicyNum;
	}

	public String getStrongPolicyNum() {
		return strongPolicyNum;
	}

	public void setStrongPolicyNum(String strongPolicyNum) {
		this.strongPolicyNum = strongPolicyNum;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getUnderwrited() {
		return underwrited;
	}

	public void setUnderwrited(String underwrited) {
		this.underwrited = underwrited;
	}

	public InsRecordNumberVO() {
		super();
	}
	
}
