package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBFlowerror extends BaseEntity implements Identifiable {
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
	 * 精灵或edi0-精灵，1-EDI
	 */
	private String firoredi;

	/**
	 * 任务类型（报价/核保/承保/支付）
	 */
	private String taskstatus;

	/**
	 * 
	 */
	private String errorcode;

	/**
	 * 
	 */
	private String errordesc;

	/**
	 * 
	 */
	private String result;

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

	public String getFiroredi() {
		return firoredi;
	}

	public void setFiroredi(String firoredi) {
		this.firoredi = firoredi;
	}

	public String getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrordesc() {
		return errordesc;
	}

	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}