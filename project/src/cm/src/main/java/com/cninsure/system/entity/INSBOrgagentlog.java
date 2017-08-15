package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBOrgagentlog extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类别，1：机构；2：代理人；3：供应商
	 */
	private Integer type;

	/**
	 * 是否成功，1：成功；2：失败
	 */
	private Integer issuccess;

	/**
	 * 同步时间
	 */
	private Date syncdate;

	/**
	 * 日志内容
	 */
	private String logcontent;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(Integer issuccess) {
		this.issuccess = issuccess;
	}

	public Date getSyncdate() {
		return syncdate;
	}

	public void setSyncdate(Date syncdate) {
		this.syncdate = syncdate;
	}

	public String getLogcontent() {
		return logcontent;
	}

	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}

}