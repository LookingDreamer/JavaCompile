package com.zzb.mobile.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadModel {
	/**
	 * 文件
	 */
	private MultipartFile file;
	/**
	 * 对应code表的codetype
	 */
	private String filetype;
	/**
	 * 任务id
	 */
	private String taskid;
	/**
	 * 文件描述
	 */
	private String filedescribe;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getFiledescribe() {
		return filedescribe;
	}
	public void setFiledescribe(String filedescribe) {
		this.filedescribe = filedescribe;
	}
	
}
