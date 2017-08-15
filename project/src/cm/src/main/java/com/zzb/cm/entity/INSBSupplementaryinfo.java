package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBSupplementaryinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 险种ID
	 */
	private String riskid;

	/**
	 * 要素名称
	 */
	private String itemname;

	/**
	 * 要素编码
	 */
	private String itemcode;

	/**
	 * 要素类型
	 */
	private String itemtype;

	/**
	 * 是否启用
	 */
	private String isusing;

	/**
	 * 报价必录
	 */
	private String isquotemust;

	/**
	 * 录入方式
	 */
	private String inputtype;

	/**
	 * 可选内容
	 */
	private String optional;

	/**
	 * 数据提核保
	 */
	private String isunderwriteusing;

	/**
	 * 核保必录
	 */
	private String isunderwritemust;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getRiskid() {
		return riskid;
	}

	public void setRiskid(String riskid) {
		this.riskid = riskid;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getIsusing() {
		return isusing;
	}

	public void setIsusing(String isusing) {
		this.isusing = isusing;
	}

	public String getIsquotemust() {
		return isquotemust;
	}

	public void setIsquotemust(String isquotemust) {
		this.isquotemust = isquotemust;
	}

	public String getInputtype() {
		return inputtype;
	}

	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String getIsunderwriteusing() {
		return isunderwriteusing;
	}

	public void setIsunderwriteusing(String isunderwriteusing) {
		this.isunderwriteusing = isunderwriteusing;
	}

	public String getIsunderwritemust() {
		return isunderwritemust;
	}

	public void setIsunderwritemust(String isunderwritemust) {
		this.isunderwritemust = isunderwritemust;
	}

}