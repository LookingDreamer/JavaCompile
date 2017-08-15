package com.cninsure.jobpool.timer;

import java.util.Date;

import org.quartz.SchedulerException;

public interface SchedulerService {
	/**
	 * 删除历史定时任务并重新启动新任务
	 * @param jobName
	 * @param taskName
	 * @param startTime
	 * @param providerId
	 * @param timeout
	 * @param dealClass 处理超时任务的job类
	 */
	public void deleteHistoryJobAndStartNewJob(String jobName,String taskName,Date startTime,String providerId,String timeout,String dealClass) throws SchedulerException;

	/**
	 * 删除指定job
	 * @param jobName
	 */
	public void deleteJob(String jobName);
	
	/**
	 * 定时任务处理轮询任务
	 * @param jobName
	 * @param taskName
	 * @param startTime
	 * @param providerId
	 * @param timeout
	 * @param dealClass 处理超时任务的job类
	 */
	public void dealLoopOverTime(String jobName,String taskName,Date startTime,String providerId,String timeout,String dealClass, int totalCount) throws SchedulerException;
	
	/**
	 * 超时任务处理逻辑
	 * @param ProInstanceId_taskName
	 * @throws SchedulerException
	 */
	public void dealOverTimeJob(String ProInstanceId_taskName);
}
