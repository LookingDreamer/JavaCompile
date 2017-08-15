package com.cninsure.system.entity;

import java.util.Date;

public class INSCCmLogs  {

	
	private int id;
	
	/**
	 * 访问
	 */
	private String loginip;

	/**
	 * 访问类名
	 */
	private String classname;

	/**
	 * 访问方法名
	 */
	private String method;

	/**
	 * 访问方法用法
	 */
	private String methodname;

	/**
	 * 方法进入时间
	 */
	private Date entertime;
	
	/**
	 * 方法结束时间
	 */
	private Date exittime;
	
	private int lasttime;

	/**
	 * 登录用户
	 */
	private String operator;

	/**
	 * 注释
	 */
	private String noti;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

 
	public Date getEntertime() {
		return entertime;
	}

	public void setEntertime(Date entertime) {
		this.entertime = entertime;
	}

	public Date getExittime() {
		return exittime;
	}

	public void setExittime(Date exittime) {
		this.exittime = exittime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getNoti() {
		return noti;
	}

	public void setNoti(String noti) {
		this.noti = noti;
	}
	
	

	public int getLasttime() {
		return lasttime;
	}

	public void setLasttime(int lasttime) {
		this.lasttime = lasttime;
	}

	@Override
	public String toString() {
		return "INSCCmLogs [id=" + id + ", loginip=" + loginip + ", classname="
				+ classname + ", method=" + method + ", methodname="
				+ methodname + ", entertime=" + entertime + ", exittime="
				+ exittime + ", lasttime=" + lasttime + ", operator="
				+ operator + ", noti=" + noti + "]";
	}




}