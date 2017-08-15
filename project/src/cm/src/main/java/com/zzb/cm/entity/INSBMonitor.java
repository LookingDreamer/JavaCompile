package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBMonitor extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 保险公司code
	 */
	private String inscomcode;

	/**
	 * 保险公司名称
	 */
	private String inscomname;

	/**
	 * 车牌号
	 */
	private String plateno;

	/**
	 * 车主id
	 */
	private String carowerid;

	/**
	 * 车主姓名
	 */
	private String carowername;

	/**
	 * 任务类型code
	 */
	private String tasktype;

	/**
	 * 任务类型名称
	 */
	private String tasktypename;

	/**
	 * 任务状态,0-执行中，1-失败，2-成功
	 */
	private String taskstatus;

	/**
	 * 任务状态描述
	 */
	private String taskstatusdes;

	/**
	 * 任务接受时间
	 */
	private Date startdate;

	/**
	 * 任务完成时间
	 */
	private Date enddate;

	/**
	 * 耗时，毫秒
	 */
	private Long usetime;

	/**
	 * 原始数据连接
	 */
	private String origindate;

	/**
	 * 结果数据连接
	 */
	private String resultdate;
	
	/**
	 * 监控id
	 */
	private String monitoeid;
	
	/**
	 * 精灵edi
	 */
	private String quotetype;
	
	
	/**
	 * 任务接受时间
	 */
	private String startdateString;

	/**
	 * 任务完成时间
	 */
	private String enddateString;
	
	public String getQuotetype() {
		return quotetype;
	}

	public void setQuotetype(String quotetype) {
		this.quotetype = quotetype;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getInscomname() {
		return inscomname;
	}

	public void setInscomname(String inscomname) {
		this.inscomname = inscomname;
	}

	public String getPlateno() {
		return plateno;
	}

	public void setPlateno(String plateno) {
		this.plateno = plateno;
	}

	public String getCarowerid() {
		return carowerid;
	}

	public void setCarowerid(String carowerid) {
		this.carowerid = carowerid;
	}

	public String getCarowername() {
		return carowername;
	}

	public void setCarowername(String carowername) {
		this.carowername = carowername;
	}

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

	public String getTasktypename() {
		return tasktypename;
	}

	public void setTasktypename(String tasktypename) {
		this.tasktypename = tasktypename;
	}

	public String getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}

	public String getTaskstatusdes() {
		return taskstatusdes;
	}

	public void setTaskstatusdes(String taskstatusdes) {
		this.taskstatusdes = taskstatusdes;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Long getUsetime() {
		return usetime;
	}

	public void setUsetime(Long usetime) {
		this.usetime = usetime;
	}

	public String getOrigindate() {
		return origindate;
	}

	public void setOrigindate(String origindate) {
		this.origindate = origindate;
	}

	public String getResultdate() {
		return resultdate;
	}

	public void setResultdate(String resultdate) {
		this.resultdate = resultdate;
	}

	public String getMonitoeid() {
		return monitoeid;
	}

	public void setMonitoeid(String monitoeid) {
		this.monitoeid = monitoeid;
	}

	public String getStartdateString() {
		return startdateString;
	}

	public void setStartdateString(String startdateString) {
		this.startdateString = startdateString;
	}

	public String getEnddateString() {
		return enddateString;
	}

	public void setEnddateString(String enddateString) {
		this.enddateString = enddateString;
	}
	
	

}