package com.zzb.cm.service;

import java.util.List;

import com.zzb.model.WorkFlow4TaskModel;

public interface INSBWorkflowDataService {

	public void startTaskFromWorlFlow(WorkFlow4TaskModel dataModel);

	public void endTaskFromWorkFlow(WorkFlow4TaskModel dataModel);

	public boolean isManWork(String taskcode);

	public String getQuoteType(Long processinstanceid, String providerid, String quotecode);
	
	public String getQuoteTypeOnly(Long processinstanceid, String providerid, String quotecode);
	/**
	 * 通过主任务跟踪获取号子任务号
	 * @param instanceid
	 * @return List<String>
	 */
	public List<String> getSubInstanceIdByInsbWorkflowMainInstanceId(String instanceid);
}
