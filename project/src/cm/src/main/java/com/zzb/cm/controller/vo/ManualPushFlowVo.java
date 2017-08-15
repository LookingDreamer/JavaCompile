package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.zzb.app.model.bean.AmountSelect;
import com.zzb.app.model.bean.SelectOption;

import java.util.List;

public class ManualPushFlowVo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务类型：sub 子任务、main 主任务
	 */
	private String type;

	/**
	 * 当前任务id
	 */
	private String instanceid;

	/**
	 * 当前任务主任务id
	 */
	private String maininstanceid;

	/**
	 * 当前任务子任务id
	 */
	private String subinstanceid;

	/**
	 * 工作流taskcode
	 */
	private String taskcode;

	/**
	 * 工作流taskname
	 */
	private String taskname;

	/**
	 * cm的taskcode
	 */
	private String cmtaskcode;

	/**
	 * cm的taskname
	 */
	private String cmtaskname;

	/**
	 * cm的taskstate
	 */
	private String cmtaskstate;

	/**
	 * 投保公司编码
	 */
	private String inscomcode;

	/**
	 * 备注
	 */
	private String remark;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getMaininstanceid() {
		return maininstanceid;
	}

	public void setMaininstanceid(String maininstanceid) {
		this.maininstanceid = maininstanceid;
	}

	public String getSubinstanceid() {
		return subinstanceid;
	}

	public void setSubinstanceid(String subinstanceid) {
		this.subinstanceid = subinstanceid;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getCmtaskcode() {
		return cmtaskcode;
	}

	public void setCmtaskcode(String cmtaskcode) {
		this.cmtaskcode = cmtaskcode;
	}

	public String getCmtaskname() {
		return cmtaskname;
	}

	public void setCmtaskname(String cmtaskname) {
		this.cmtaskname = cmtaskname;
	}

	public String getCmtaskstate() {
		return cmtaskstate;
	}

	public void setCmtaskstate(String cmtaskstate) {
		this.cmtaskstate = cmtaskstate;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
