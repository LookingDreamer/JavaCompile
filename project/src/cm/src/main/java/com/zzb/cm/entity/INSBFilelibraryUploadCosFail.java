package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;



public class INSBFilelibraryUploadCosFail extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 影像类型-原图proto小图small
	 */
	private String imagetype;

	/**
	 * 附件库id
	 */
	private String filelibraryid;

	/**
	 * 文件物理路径
	 */
	private String filephysicalpath;
	
	public String getFilephysicalpath() {
		return filephysicalpath;
	}

	public void setFilephysicalpath(String filephysicalpath) {
		this.filephysicalpath = filephysicalpath;
	}

	public String getImagetype() {
		return imagetype;
	}

	public void setImagetype(String imagetype) {
		this.imagetype = imagetype;
	}

	public String getFilelibraryid() {
		return filelibraryid;
	}

	public void setFilelibraryid(String filelibraryid) {
		this.filelibraryid = filelibraryid;
	}

}