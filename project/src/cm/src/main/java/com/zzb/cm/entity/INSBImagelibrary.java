package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBImagelibrary extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 代理人编码
	 */
	private String agentcode;

	/**
	 * 被保人id
	 */
	private String insuredid;

	/**
	 * 被保人姓名
	 */
	private String insuredname;

	/**
	 * 文件描述
	 */
	private String filedescribe;

	/**
	 * 文件分类，如被保人身份证正面
	 */
	private Integer fileclassification;

	/**
	 * 文件路径
	 */
	private String filepath;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAgentcode() {
		return agentcode;
	}

	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}

	public String getInsuredid() {
		return insuredid;
	}

	public void setInsuredid(String insuredid) {
		this.insuredid = insuredid;
	}

	public String getInsuredname() {
		return insuredname;
	}

	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}

	public String getFiledescribe() {
		return filedescribe;
	}

	public void setFiledescribe(String filedescribe) {
		this.filedescribe = filedescribe;
	}

	public Integer getFileclassification() {
		return fileclassification;
	}

	public void setFileclassification(Integer fileclassification) {
		this.fileclassification = fileclassification;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

}