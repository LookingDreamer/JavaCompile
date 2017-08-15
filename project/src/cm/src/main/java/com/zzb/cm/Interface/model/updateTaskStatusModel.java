package com.zzb.cm.Interface.model;

public class updateTaskStatusModel {

private String 	taskId;

private String taskType;

private String taskStatus;

private String processType;

public String getTaskId() {
	return taskId;
}

public void setTaskId(String taskId) {
	this.taskId = taskId;
}

public String getTaskType() {
	return taskType;
}

public void setTaskType(String taskType) {
	this.taskType = taskType;
}

public String getTaskStatus() {
	return taskStatus;
}

public void setTaskStatus(String taskStatus) {
	this.taskStatus = taskStatus;
}

public String getProcessType() {
	return processType;
}

public void setProcessType(String processType) {
	this.processType = processType;
}

}
