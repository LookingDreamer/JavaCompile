package com.zzb.cm.controller.vo;

import java.util.Date;


public class OrderListVo {
	//业管ID
	private String id;
	//车牌
	private String platenumber;
	//被保人姓名
	private String name;
	//报价公司
	private String prvshotname;
	//创建时间
	private Date createtime;
	//订单状态 0为全部，1为待投保，2为待支付
	private String orderstatus;
	//开始时间
	private Date fromtime;
	private String from;
	//结束时间
	private Date totime;
	private String to;
	private String taskid;
	
	public Date getFromtime() {
		return fromtime;
	}
	public void setFromtime(Date fromtime) {
		this.fromtime = fromtime;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public Date getTotime() {
		return totime;
	}
	public void setTotime(Date totime) {
		this.totime = totime;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public OrderListVo(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlatenumber() {
		return platenumber;
	}
	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrvshotname() {
		return prvshotname;
	}
	public void setPrvshotname(String prvshotname) {
		this.prvshotname = prvshotname;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
