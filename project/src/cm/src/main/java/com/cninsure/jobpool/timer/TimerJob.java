package com.cninsure.jobpool.timer;

import org.quartz.Job;

import com.cninsure.jobpool.Task;


/**
 * 定时器工作信息
 * @author hxx
 *
 */
public class TimerJob {
	/**
	 * 执行标记 true 需要执行
	 */
	private Task task;
	private boolean runflag ;
	private String startDate;
	private String excDate;
	private Class <? extends Job> jobClass;//job 执行者
	
	public boolean isRunflag() {
		return runflag;
	}
	public void setRunflag(boolean runflag) {
		this.runflag = runflag;
	}
	
	public Class<? extends Job> getJobClass() {
		return jobClass;
	}
	public void setJobClass(Class<? extends Job> jobClass) {
		this.jobClass = jobClass;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExcDate() {
		return excDate;
	}
	public void setExcDate(String excDate) {
		this.excDate = excDate;
	}
	@Override
	public String toString() {
		return "TimerJob [task=" + task + ", runflag=" + runflag
				+ ", startDate=" + startDate + ", excDate=" + excDate
				+ ", jobClass=" + jobClass + "]";
	}
	
	
}
 