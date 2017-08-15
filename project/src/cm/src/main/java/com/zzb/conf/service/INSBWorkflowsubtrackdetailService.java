package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;

public interface INSBWorkflowsubtrackdetailService extends BaseService<INSBWorkflowsubtrackdetail> {
	

	/**
	 * 
	 * 
	 * 主流程子流程统一判断更新
	 * 
	 * 
	 * 1:(不做处理)EDI报价成功、精灵报价成功、EDI核保暂存成功、精灵暂存成功、人工报价业管处理完成、人工核保业管处理完成
	 * 
	 * 2:（超时）目前系统设置了120秒的超时限制，例如EDI报价120秒未报出价格则系统自动转入下一个流程
	 * 
	 * 3 :代理人在前端直接取消了订单，导致所有任务在当前阶段直接中止；
	 * 
	 * 4:系统自动中止：一个主任务下面有多个子任务，若其中一个进入核保阶段，则其他还在报价阶段的任务将被系统自动中止于当前阶段，将表中由于这种原因完成状态（taskstate）标识为‘Completed’ 的流程标识为此种状态；
	 * 
	 * 回写	代理人取消
	 * 
	 * 系统关闭、代理人取消、异常终止、成功、失败、暂存成功、暂存失败
	 * 
	 * @param param  mainTaskId subTaskId taskFeedBack	taskState taskCode
	 * @return
	 */
	public boolean addWorkFlowEndStatus(Map<String,String> param);

    /**
     * 上一个方法的参数封闭版
     * @param mainTaskId
     * @param subTaskId
     * @param taskFeedBack
     * @param taskCode
     * @param taskState
     * @param taskName
     * @param operator
     * @return
     */
    public boolean addWorkFlowEndStatus(String mainTaskId, String subTaskId, String taskFeedBack, String taskCode, String taskState, String taskName, String operator);
}