package com.zzb.cm.entity;

import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class INSBWorkflowtask extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	 
		 private Integer  archived;
		 private String allowedToDelegate;
		 private String description;
		 private String formName;
		 private String name;
		 private Integer priority;
		 private String subTaskStrategy;
		 private String subject;
		 private Date  activationTime;
		 private Date createdOn;
		 private String deploymentId;
		 private Integer documentAccessType;
		 private Integer documentContentId;
		 private String documentType;
		 private Date expirationTime;
		 private Integer  faultAccessType;
		 private Integer faultContentId;
		 private String faultName;
		 private String faultType;
		 private Integer outputAccessType;
		 private Integer outputContentId;
		 private String outputType;
		 private Integer parentId;
	     private Integer previousStatus;
		 private String processId;
		 private Integer processInstanceId;
		 private Integer processSessionId;
		 private Integer skipable;
		 private String status;
		 private Integer workItemId;
		 private String taskType;
		 private Integer OPTLOCK;
		 private String taskInitiator_id;
		 private String actualOwner_id;
		 private String createdBy_id;
		public Integer getArchived() {
			return archived;
		}
		public void setArchived(Integer archived) {
			this.archived = archived;
		}
		public String getAllowedToDelegate() {
			return allowedToDelegate;
		}
		public void setAllowedToDelegate(String allowedToDelegate) {
			this.allowedToDelegate = allowedToDelegate;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getFormName() {
			return formName;
		}
		public void setFormName(String formName) {
			this.formName = formName;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getPriority() {
			return priority;
		}
		public void setPriority(Integer priority) {
			this.priority = priority;
		}
		public String getSubTaskStrategy() {
			return subTaskStrategy;
		}
		public void setSubTaskStrategy(String subTaskStrategy) {
			this.subTaskStrategy = subTaskStrategy;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public Date getActivationTime() {
			return activationTime;
		}
		public void setActivationTime(Date activationTime) {
			this.activationTime = activationTime;
		}
		public Date getCreatedOn() {
			return createdOn;
		}
		public void setCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
		}
		public String getDeploymentId() {
			return deploymentId;
		}
		public void setDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
		}
		public Integer getDocumentAccessType() {
			return documentAccessType;
		}
		public void setDocumentAccessType(Integer documentAccessType) {
			this.documentAccessType = documentAccessType;
		}
		public Integer getDocumentContentId() {
			return documentContentId;
		}
		public void setDocumentContentId(Integer documentContentId) {
			this.documentContentId = documentContentId;
		}
		public String getDocumentType() {
			return documentType;
		}
		public void setDocumentType(String documentType) {
			this.documentType = documentType;
		}
		public Date getExpirationTime() {
			return expirationTime;
		}
		public void setExpirationTime(Date expirationTime) {
			this.expirationTime = expirationTime;
		}
		public Integer getFaultAccessType() {
			return faultAccessType;
		}
		public void setFaultAccessType(Integer faultAccessType) {
			this.faultAccessType = faultAccessType;
		}
		public Integer getFaultContentId() {
			return faultContentId;
		}
		public void setFaultContentId(Integer faultContentId) {
			this.faultContentId = faultContentId;
		}
		public String getFaultName() {
			return faultName;
		}
		public void setFaultName(String faultName) {
			this.faultName = faultName;
		}
		public String getFaultType() {
			return faultType;
		}
		public void setFaultType(String faultType) {
			this.faultType = faultType;
		}
		public Integer getOutputAccessType() {
			return outputAccessType;
		}
		public void setOutputAccessType(Integer outputAccessType) {
			this.outputAccessType = outputAccessType;
		}
		public Integer getOutputContentId() {
			return outputContentId;
		}
		public void setOutputContentId(Integer outputContentId) {
			this.outputContentId = outputContentId;
		}
		public String getOutputType() {
			return outputType;
		}
		public void setOutputType(String outputType) {
			this.outputType = outputType;
		}
		public Integer getParentId() {
			return parentId;
		}
		public void setParentId(Integer parentId) {
			this.parentId = parentId;
		}
		public Integer getPreviousStatus() {
			return previousStatus;
		}
		public void setPreviousStatus(Integer previousStatus) {
			this.previousStatus = previousStatus;
		}
		public String getProcessId() {
			return processId;
		}
		public void setProcessId(String processId) {
			this.processId = processId;
		}
		public Integer getProcessInstanceId() {
			return processInstanceId;
		}
		public void setProcessInstanceId(Integer processInstanceId) {
			this.processInstanceId = processInstanceId;
		}
		public Integer getProcessSessionId() {
			return processSessionId;
		}
		public void setProcessSessionId(Integer processSessionId) {
			this.processSessionId = processSessionId;
		}
		public Integer getSkipable() {
			return skipable;
		}
		public void setSkipable(Integer skipable) {
			this.skipable = skipable;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Integer getWorkItemId() {
			return workItemId;
		}
		public void setWorkItemId(Integer workItemId) {
			this.workItemId = workItemId;
		}
		public String getTaskType() {
			return taskType;
		}
		public void setTaskType(String taskType) {
			this.taskType = taskType;
		}
		public Integer getOPTLOCK() {
			return OPTLOCK;
		}
		public void setOPTLOCK(Integer oPTLOCK) {
			OPTLOCK = oPTLOCK;
		}
		public String getTaskInitiator_id() {
			return taskInitiator_id;
		}
		public void setTaskInitiator_id(String taskInitiator_id) {
			this.taskInitiator_id = taskInitiator_id;
		}
		public String getActualOwner_id() {
			return actualOwner_id;
		}
		public void setActualOwner_id(String actualOwner_id) {
			this.actualOwner_id = actualOwner_id;
		}
		public String getCreatedBy_id() {
			return createdBy_id;
		}
		public void setCreatedBy_id(String createdBy_id) {
			this.createdBy_id = createdBy_id;
		}
	     
}
