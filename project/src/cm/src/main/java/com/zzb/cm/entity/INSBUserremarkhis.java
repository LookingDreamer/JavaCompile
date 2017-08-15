package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBUserremarkhis extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 
	 */
	private String remarktype;

	/**
	 * 
	 */
	private String remarkcontent;

	/**
	 * 
	 */
	private String remark;

	/**
	 * 
	 */
	private String remarkattr;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getRemarktype() {
		return remarktype;
	}

	public void setRemarktype(String remarktype) {
		this.remarktype = remarktype;
	}

	public String getRemarkcontent() {
		return remarkcontent;
	}

	public void setRemarkcontent(String remarkcontent) {
		this.remarkcontent = remarkcontent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkattr() {
		return remarkattr;
	}

	public void setRemarkattr(String remarkattr) {
		this.remarkattr = remarkattr;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

}