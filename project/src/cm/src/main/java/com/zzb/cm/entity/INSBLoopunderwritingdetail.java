package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBLoopunderwritingdetail extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务轮询信息表主键
	 */
	private String loopid;

	/**
	 * 轮询开始时间
	 */
	private Date starttime;

	/**
	 * 轮询结果
	 */
	private String loopresult;

	/**
	 * 结果详情
	 */
	private String msg;

	public String getLoopid() {
		return loopid;
	}

	public void setLoopid(String loopid) {
		this.loopid = loopid;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public String getLoopresult() {
		return loopresult;
	}

	public void setLoopresult(String loopresult) {
		this.loopresult = loopresult;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}