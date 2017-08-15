package com.zzb.conf.controller.vo;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String status ;// "0K",  //状态码, OK:成功,ERROR:失败
	private String message;
	private Object result;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public String toString(){
		JSONObject json =JSONObject.fromObject(this);
		return json.toString();
	}
}
