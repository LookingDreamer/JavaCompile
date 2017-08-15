package com.zzb.model;

/**
 * 
 * 接收工作流信息
 * 
 * @author Administrator
 *
 */
public class WorkFlow4TaskModel {

	private String mainInstanceId; // 主实例ID
	private String subInstanceId; // 子实例ID
	private String providerId;// 供应商编码
	private String taskName;// 节点名称
	private String taskCode;
	private String taskStatus;//节点状态
	private int dataFlag;//数据类型 1：节点状态和任务同时更新 2：只更新节点状态不开启任务

	public String getMainInstanceId() {
		return mainInstanceId;
	}

	public void setMainInstanceId(String mainInstanceId) {
		this.mainInstanceId = mainInstanceId;
	}

	public String getSubInstanceId() {
		return subInstanceId;
	}

	public void setSubInstanceId(String subInstanceId) {
		this.subInstanceId = subInstanceId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	
	
	public int getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(int dataFlag) {
		this.dataFlag = dataFlag;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	
	

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	@Override
	public String toString() {
		return "WorkFlow4TaskModel [mainInstanceId=" + mainInstanceId
				+ ", subInstanceId=" + subInstanceId + ", providerId="
				+ providerId + ", taskName=" + taskName + ", taskCode="
				+ taskCode + ", taskStatus=" + taskStatus + ", dataFlag="
				+ dataFlag + "]";
	}
}
