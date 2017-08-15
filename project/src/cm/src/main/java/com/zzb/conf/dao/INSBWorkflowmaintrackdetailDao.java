package com.zzb.conf.dao;

import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;

public interface INSBWorkflowmaintrackdetailDao extends BaseDao<INSBWorkflowmaintrackdetail> {

	public void myInsert(INSBWorkflowmain model);
	
	/**
	 * bug 1338
	 * 
	 * @param pram  String instanceId,String taskCode
	 * @return
	 */
	public INSBWorkflowmaintrackdetail selectData4RunQian(Map<String,String> param);
	
	

	/**
	 * 当前是completed 按照逻辑主键查找
	 */
	public INSBWorkflowmaintrackdetail copyTaskFeed4CompletedState(String instanceId);
}