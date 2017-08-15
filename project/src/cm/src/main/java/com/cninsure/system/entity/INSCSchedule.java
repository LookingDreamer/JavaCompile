package com.cninsure.system.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.cninsure.core.utils.DateUtil;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class INSCSchedule extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务类别ID
	 */
	private String tasktypeid;

	/**
	 * 任务ID
	 */
	private String taskid;

	/**
	 * 任务类别名称
	 */
	private String tasktypename;

	/**
	 * 任务名称
	 */
	private String taskname;

	/**
	 * 起始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date starttime;

	/**
	 * 下次运行时间
	 */
	private Date nexttime;

	/**
	 * 是否使用cron
	 */
	private Integer iscron;

	/**
	 * 周期
	 */
	private Integer period;

	/**
	 * 周期单位
	 */
	private String periodunit;

	/**
	 * cron表达式
	 */
	private String cronexp;

	/**
	 * 是否启用,1：启用；2：停用
	 */
	private Integer state;

	/**
	 * IP
	 */
	private String ip;

	/**
	 * 描述
	 */
	private String comment;
	
	/**
	 * 起始时间
	 */
	private String starttimeStr;

	public String getTasktypeid() {
		return tasktypeid;
	}

	public void setTasktypeid(String tasktypeid) {
		this.tasktypeid = tasktypeid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTasktypename() {
		return tasktypename;
	}

	public void setTasktypename(String tasktypename) {
		this.tasktypename = tasktypename;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getNexttime() {
		return nexttime;
	}

	public void setNexttime(Date nexttime) {
		this.nexttime = nexttime;
	}

	public Integer getIscron() {
		return iscron;
	}

	public void setIscron(Integer iscron) {
		this.iscron = iscron;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getPeriodunit() {
		return periodunit;
	}

	public void setPeriodunit(String periodunit) {
		this.periodunit = periodunit;
	}

	public String getCronexp() {
		return cronexp;
	}

	public void setCronexp(String cronexp) {
		this.cronexp = cronexp;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStarttimeStr() {
		starttimeStr = DateUtil.toDateTimeString(starttime);
		return starttimeStr;
	}

	public void setStarttimeStr(String starttimeStr) {
		this.starttimeStr = starttimeStr;
	}

}