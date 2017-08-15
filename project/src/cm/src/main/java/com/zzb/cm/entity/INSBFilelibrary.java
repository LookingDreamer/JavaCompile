package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;



public class INSBFilelibrary extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 文件类型-对应字典
	 */
	private String filetype;

	/**
	 * 文件名称-对应字典
	 */
	private String filename;

	/**
	 * 文件分类-对应字典
	 */
	private String filecodevalue;

	/**
	 * 文件描述
	 */
	private String filedescribe;

	/**
	 * 文件路径
	 */
	private String filepath;
	/**
	 * 文件物理路径
	 */
	private String filephysicalpath;
	
	/**
	 * 缩略图路径
	 */
	private String smallfilepath;

	public String getFilephysicalpath() {
		return filephysicalpath;
	}

	public void setFilephysicalpath(String filephysicalpath) {
		this.filephysicalpath = filephysicalpath;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilecodevalue() {
		return filecodevalue;
	}

	public void setFilecodevalue(String filecodevalue) {
		this.filecodevalue = filecodevalue;
	}

	public String getFiledescribe() {
		return filedescribe;
	}

	public void setFiledescribe(String filedescribe) {
		this.filedescribe = filedescribe;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getSmallfilepath() {
		return smallfilepath;
	}

	public void setSmallfilepath(String smallfilepath) {
		this.smallfilepath = smallfilepath;
	}

}