package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBMinimsglog extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private String agentid;

	/**
	 * 消息id
	 */
	private String msgid;

	/**
	 * 微信返回的code
	 */
	private String errcode;

	/**
	 * 微信返回的消息
	 */
	private String errmsg;

	/**
	 * 消息类型
	 */
	private String msgtype;

	/**
	 * 交易任务号
	 */
	private String taskid;

	/**
	 * 状态
	 */
	private String status;

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}