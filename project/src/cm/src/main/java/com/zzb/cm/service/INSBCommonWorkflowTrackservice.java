package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBWorkflowmaintrack;

public interface INSBCommonWorkflowTrackservice extends BaseService<INSBWorkflowmaintrack>{

	/**
	 * 工作流程图获得信息方法 
	 * */
	public List<Map<String, Object>> getWorkFlowTrack(String instanceid);
	
	/**
	 * 工作流程图获得信息方法 （适用子流程）
	 * */
	public List<Map<String, Object>> getWorkFlowTrack(String instanceid, String incomcode);

}
