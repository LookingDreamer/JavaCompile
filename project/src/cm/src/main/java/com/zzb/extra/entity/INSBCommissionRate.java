package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBCommissionRate extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商协议id
	 */
	private String agreementid;

	/**
	 * 佣金类型
	 */
	private String commissiontype;

	/**
	 * 佣金率值
	 */
	private Double rate;

	/**
	 * 生效时间
	 */
	private Date effectivetime;

    /**
     * 结束时间
     */
    private Date terminaltime;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 佣金备注
	 */
	private String remark;

	/**
	 * 佣金提醒
	 */
	private String reminder;

	/**
	 * 产品代码
	 */

	private String productcode;

	/**
	 * 应用渠道
	 */

	private String channelsource;

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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getEffectivetime() {
		return effectivetime;
	}

	public void setEffectivetime(Date effectivetime) {
		this.effectivetime = effectivetime;
	}

    public Date getTerminaltime() {
        return terminaltime;
    }

    public void setTerminaltime(Date terminaltime) {
        this.terminaltime = terminaltime;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}


	public String getChannelsource() {
		return channelsource;
	}

	public void setChannelsource(String channelsource) {
		this.channelsource = channelsource;
	}


}