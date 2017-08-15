package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBMiniOrderTrace extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务号
	 */
	private String taskid;

	/**
	 * 用户ID
	 */
	private String agentid;

	/**
	 * 被保人姓名
	 */
	private String insuredname;

	/**
	 * 车主
	 */
	private String carowner;

	/**
	 * 车牌号
	 */
	private String carlicenseno;

	/**
	 * 投保公司
	 */
	private String prvname;

	/**
	 * 保费
	 */
	private String premium;

	/**
	 * 支付时间
	 */
	private String paytime;

	/**
	 * 起保时间
	 */
	private String starttime;

	/**
	 * 订单状态
	 */
	private String taskstate;

	/**
	 * 处理状态
	 */
	private String operatestate;

	/**
	 * 核保失败原因
	 */
	private String msg;

	/**
	 * 退款理由
	 */
	private String reason;

	private String providercode;

	/**
	 * 用户是否自行操作
	 */
	private String agentoperate;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getInsuredname() {
		return insuredname;
	}

	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}

	public String getCarowner() {
		return carowner;
	}

	public void setCarowner(String carowner) {
		this.carowner = carowner;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}

	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}

	public String getPrvname() {
		return prvname;
	}

	public void setPrvname(String prvname) {
		this.prvname = prvname;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getTaskstate() {
		return taskstate;
	}

	public void setTaskstate(String taskstate) {
		this.taskstate = taskstate;
	}

	public String getOperatestate() {
		return operatestate;
	}

	public void setOperatestate(String operatestate) {
		this.operatestate = operatestate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getProvidercode() {
		return providercode;
	}

	public void setProvidercode(String providercode) {
		this.providercode = providercode;
	}


	public String getAgentoperate() {
		return agentoperate;
	}

	public void setAgentoperate(String agentoperate) {
		this.agentoperate = agentoperate;
	}


}