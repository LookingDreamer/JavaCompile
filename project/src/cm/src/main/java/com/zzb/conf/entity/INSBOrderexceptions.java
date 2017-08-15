package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBOrderexceptions extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 订单表id
	 */
	private String orderid;

	/**
	 * 异常类型
	 */
	private String exceptiontype;

	/**
	 * 异常内容
	 */
	private String exceptioncontent;


	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getExceptiontype() {
		return exceptiontype;
	}

	public void setExceptiontype(String exceptiontype) {
		this.exceptiontype = exceptiontype;
	}

	public String getExceptioncontent() {
		return exceptioncontent;
	}

	public void setExceptioncontent(String exceptioncontent) {
		this.exceptioncontent = exceptioncontent;
	}

}