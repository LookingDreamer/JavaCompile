package com.zzb.cm.Interface.entity.car.model;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**错误编码，从0开始*/
	private Integer errorcode;
	/**错误描述*/
	private String errordesc;
	/**是否是阻断性的错误：如：重复投保等（true），
	 * 即无法通过修改数据或者更改投保保险公司而解决的问题属于阻断性错误，
	 * 非阻断性（false），即缺失投保报价的关键信息等错误，通过人工填写缺失信息可以解决的错误*/
	private Boolean stop;

	public Integer getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrordesc() {
		return errordesc;
	}
	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}
	public Boolean getStop() {
		return stop;
	}
	public void setStop(Boolean stop) {
		this.stop = stop;
	}

}
