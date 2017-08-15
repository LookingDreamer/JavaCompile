package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBFlowinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 当前处理
	 */
	private String currenthandle;

	/**
	 * 当前流程节点编码
	 */
	private String flowcode;

	/**
	 * 当前流程节点名称
	 */
	private String flowname;

	/**
	 * 前一个流程节点编码
	 */
	private String overflowcode;

	/**
	 * 前一个流程节点名称
	 */
	private String overflowname;

	/**
	 * 精灵或edi 0-精灵，1-EDI
	 */
	private String firoredi;

	/**
	 * 是否锁定 0-未锁定 1-锁定
	 */
	private String islock;

	/**
	 * 
	 */
	private String pop1;

	/**
	 * 
	 */
	private String pop2;
	/**
	 * 任务类型（报价/核保/承保/支付）
	 */
	private String taskstatus;

	public String getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getCurrenthandle() {
		return currenthandle;
	}

	public void setCurrenthandle(String currenthandle) {
		this.currenthandle = currenthandle;
	}

	public String getFlowcode() {
		return flowcode;
	}

	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}

	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}

	public String getOverflowcode() {
		return overflowcode;
	}

	public void setOverflowcode(String overflowcode) {
		this.overflowcode = overflowcode;
	}

	public String getOverflowname() {
		return overflowname;
	}

	public void setOverflowname(String overflowname) {
		this.overflowname = overflowname;
	}

	/**
	 * 精灵或edi0-精灵，1-EDI
	 */
	public String getFiroredi() {
		return firoredi;
	}
	/**
	 * 精灵或edi0-精灵，1-EDI
	 */
	public void setFiroredi(String firoredi) {
		this.firoredi = firoredi;
	}
	/**
	 * 是否锁定0-未锁定1-锁定
	 */
	public String getIslock() {
		return islock;
	}
	/**
	 * 是否锁定0-未锁定1-锁定
	 */
	public void setIslock(String islock) {
		this.islock = islock;
	}

	public String getPop1() {
		return pop1;
	}

	public void setPop1(String pop1) {
		this.pop1 = pop1;
	}

	public String getPop2() {
		return pop2;
	}

	public void setPop2(String pop2) {
		this.pop2 = pop2;
	}

}