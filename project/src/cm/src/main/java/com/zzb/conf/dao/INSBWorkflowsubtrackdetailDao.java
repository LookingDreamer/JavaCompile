package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;

public interface INSBWorkflowsubtrackdetailDao extends BaseDao<INSBWorkflowsubtrackdetail> {
	
	/**
	 * 改造新增方法
	 * @param model
	 */
	public void myInsert(INSBWorkflowsub model);
	
	
	
	/**
	 * 通过子流程id拿到当前最新记录
	 * @return
	 */
	public INSBWorkflowsubtrackdetail selectLastModelBySubInstanceId(Map<String,String> param);

	public List<INSBWorkflowsubtrackdetail> selectUpdatableData4RunQian(Map<String,String> param);

	/**
	 * bug 1338
	 * 
	 * @param param  String instanceId,String taskCode
	 * @return
	 */
	public List<INSBWorkflowsubtrackdetail> selectData4RunQian(Map<String,String> param);
	
	public List<INSBWorkflowsubtrackdetail> selectData4RunCompleted(Map<String, String> param);
	/**
	 * 当前是completed 按照逻辑主键查找
	 */
	public List<INSBWorkflowsubtrackdetail> copyTaskFeed4CompletedState(String instanceId);

    public List<INSBWorkflowsubtrackdetail> selectLockConf(Map<String, Object> map);

    public INSBWorkflowsubtrackdetail findPrevWorkFlow(String instanceid, String taskcode);

    public List<INSBWorkflowsubtrackdetail> selectAllBy(Map<String, Object> params);
}