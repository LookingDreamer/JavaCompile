package com.zzb.mobile.model;

import java.util.List;

public class SaveUploadImageModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 文件id
	 */
	private List<String> fileids;
	/**
	 * 操作人
	 */
	private String operator;
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public List<String> getFileids() {
		return fileids;
	}

	public void setFileids(List<String> fileids) {
		this.fileids = fileids;
	}

}
