package com.zzb.chn.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBCommissionratio extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道id
	 */
	private String channelid;

	/**
	 * 佣金类型
	 */
	private String commissiontype;

	/**
	 * 佣金系数
	 */
	private Double ratio;

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
	 * 
	 */
	private String remark;
	/**
	 *计算方式
	 */
	private String calculatetype;
	/**
	 *税率
	 */
	private Double taxrate;

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getCommissiontype() {
		return commissiontype;
	}

	public void setCommissiontype(String commissiontype) {
		this.commissiontype = commissiontype;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
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


	public Double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}

	public String getCalculatetype() {
		return calculatetype;
	}

	public void setCalculatetype(String calculatetype) {
		this.calculatetype = calculatetype;
	}


}