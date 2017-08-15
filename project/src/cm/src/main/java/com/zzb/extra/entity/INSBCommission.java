package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCommission extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

    /**
     * 供应商协议id
     */
    private String agreementid;

    /**
     * 佣金类型
     */
    private String commissiontype;

	/**
	 * 佣金率id
	 */
	private String commissionrateid;

	/**
	 * 佣金值
	 */
	private Double counts;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 供应商编号
	 */
	private String providercode;

	/**
	 * 佣金系数编号
	 */
	private String commissionRatioId;

	/**
	 * 佣金系数编号
	 */
	private String channelId;

	/**
	 * 佣金标示 报价、承保
	 */

	private String commissionFlag = "quote";

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }

    public String getCommissiontype() {
        return commissiontype;
    }

    public void setCommissiontype(String commissiontype) {
        this.commissiontype = commissiontype;
    }

	public String getCommissionrateid() {
		return commissionrateid;
	}

	public void setCommissionrateid(String commissionrateid) {
		this.commissionrateid = commissionrateid;
	}

	public Double getCounts() {
		return counts;
	}

	public void setCounts(Double counts) {
		this.counts = counts;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getCommissionFlag() {
		return commissionFlag;
	}

	public void setCommissionFlag(String commissionFlag) {
		this.commissionFlag = commissionFlag;
	}

	public String getProvidercode() {
		return providercode;
	}

	public void setProvidercode(String providercode) {
		this.providercode = providercode;
	}


	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCommissionRatioId() {
		return commissionRatioId;
	}

	public void setCommissionRatioId(String commissionRatioId) {
		this.commissionRatioId = commissionRatioId;
	}
}