package com.cninsure.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zzb.cm.pollingtask.OrderTaskPolling;

public class LzgOrderTaskPollingJob implements Job {
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		OrderTaskPolling orderTaskPolling = new OrderTaskPolling();
		if (orderTaskPolling != null) {
			orderTaskPolling.orderPollingTask();
		}
	}
}
